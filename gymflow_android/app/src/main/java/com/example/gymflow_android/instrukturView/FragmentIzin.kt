package com.example.gymflow_android.instrukturView

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gymflow_android.R
import com.example.gymflow_android.api.RetrofitClient
import com.example.gymflow_android.models.IzinRequest
import com.example.gymflow_android.models.JadwalHarian
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FragmentIzin : Fragment() {

    private var edJadwal: AutoCompleteTextView? = null
    private var etAlasan: TextInputEditText? = null
    private var btnSubmit: MaterialButton? = null

    private val myPreference = "myPref"
    private var userId: Int = -1
    private var jadwalList: List<JadwalHarian> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_izin, container, false)

        edJadwal  = view.findViewById(R.id.edJadwal)
        etAlasan  = view.findViewById(R.id.etAlasan)
        btnSubmit = view.findViewById(R.id.btn_submit)

        edJadwal?.isFocusable = false

        userId = requireContext()
            .getSharedPreferences(myPreference, Context.MODE_PRIVATE)
            .getInt("userId", -1)

        loadJadwal()

        btnSubmit?.setOnClickListener { submitIzin() }

        return view
    }

    private fun loadJadwal() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getJadwalHarianByInstruktur(userId)
                if (response.isSuccessful && response.body() != null) {
                    jadwalList = response.body()!!.data
                    val labels = jadwalList.map { "${it.nama_kelas} - ${it.hari} ${it.jam}" }
                    edJadwal?.setAdapter(
                        ArrayAdapter(requireContext(), R.layout.item_list, labels)
                    )
                } else {
                    Toast.makeText(requireContext(), "Tidak ada jadwal mendatang.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitIzin() {
        val selectedLabel = edJadwal?.text.toString()
        val alasan        = etAlasan?.text.toString().trim()

        if (selectedLabel.isEmpty()) {
            Toast.makeText(requireContext(), "Pilih jadwal terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }
        if (alasan.isEmpty()) {
            Toast.makeText(requireContext(), "Alasan tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIndex = (edJadwal?.adapter as? ArrayAdapter<*>)
            ?.let { adapter ->
                (0 until adapter.count).firstOrNull { adapter.getItem(it) == selectedLabel }
            } ?: -1

        if (selectedIndex == -1 || selectedIndex >= jadwalList.size) {
            Toast.makeText(requireContext(), "Jadwal tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        val jadwal = jadwalList[selectedIndex]

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                btnSubmit?.isEnabled = false
                val response = RetrofitClient.api.createIzin(
                    IzinRequest(
                        id_instruktur    = userId,
                        id_jadwal_harian = jadwal.id_jadwal_harian,
                        detail_izin      = alasan,
                        tanggal_izin     = jadwal.hari
                    )
                )
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Izin berhasil diajukan!", Toast.LENGTH_SHORT).show()
                    edJadwal?.setText("")
                    etAlasan?.setText("")
                    loadJadwal()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengajukan izin.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                btnSubmit?.isEnabled = true
            }
        }
    }
}
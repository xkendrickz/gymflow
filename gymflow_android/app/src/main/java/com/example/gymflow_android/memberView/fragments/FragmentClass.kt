package com.example.gymflow_android.memberView.fragments

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
import com.example.gymflow_android.models.BookingKelas
import com.example.gymflow_android.models.JadwalHarian
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentClass : Fragment() {

    private var edKelas: AutoCompleteTextView? = null
    private val myPreference = "myPref"
    private var userId: Int = -1
    private var jadwalList: List<JadwalHarian> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_class, container, false)

        edKelas = view.findViewById(R.id.edKelas)
        edKelas?.isFocusable = false

        userId = requireContext()
            .getSharedPreferences(myPreference, Context.MODE_PRIVATE)
            .getInt("userId", -1)

        loadJadwalHariIni()

        view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_checkin)
            .setOnClickListener { checkIn() }

        return view
    }

    private fun loadJadwalHariIni() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getJadwalHarian(today)
                if (response.isSuccessful && response.body() != null) {
                    jadwalList = response.body()!!.data
                    val labels = jadwalList.map {
                        "${it.nama_kelas} - ${it.jam} (${it.nama_instruktur})"
                    }
                    edKelas?.setAdapter(
                        ArrayAdapter(requireContext(), R.layout.item_list, labels)
                    )
                } else {
                    Toast.makeText(requireContext(), "Tidak ada jadwal hari ini.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIn() {
        val selectedLabel = edKelas?.text.toString()

        if (selectedLabel.isEmpty()) {
            Toast.makeText(requireContext(), "Pilih kelas terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIndex = (edKelas?.adapter as? ArrayAdapter<*>)
            ?.let { adapter ->
                (0 until adapter.count).firstOrNull { adapter.getItem(it) == selectedLabel }
            } ?: -1

        if (selectedIndex == -1 || selectedIndex >= jadwalList.size) {
            Toast.makeText(requireContext(), "Kelas tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        val jadwal = jadwalList[selectedIndex]

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createPresensiKelas(
                    BookingKelas(id_member = userId, id_jadwal_harian = jadwal.id_jadwal_harian)
                )
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Check-in berhasil!", Toast.LENGTH_SHORT).show()
                    edKelas?.setText("")
                } else {
                    Toast.makeText(requireContext(), "Gagal check-in.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
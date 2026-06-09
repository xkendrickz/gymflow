package com.example.gymflow_android.pegawaiView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymflow_android.R
import com.example.gymflow_android.adapters.PresensiGymAdapter
import com.example.gymflow_android.api.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentPresencePegawai : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var adapter: PresensiGymAdapter

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_presence_pegawai, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar  = view.findViewById(R.id.progressBar)
        layoutEmpty  = view.findViewById(R.id.layoutEmpty)
        recyclerView = view.findViewById(R.id.recyclerView)

        adapter = PresensiGymAdapter(onCheckin = { item -> checkinMember(item.id_booking_gym) })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadPresensi()
    }

    private fun loadPresensi() {
        progressBar.visibility  = View.VISIBLE
        layoutEmpty.visibility  = View.GONE
        recyclerView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getPresensiGym()
                if (response.isSuccessful && response.body() != null) {
                    val todayData = response.body()!!.data
                        .filter { it.tanggal == today }

                    if (todayData.isEmpty()) {
                        layoutEmpty.visibility  = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        adapter.setData(todayData)
                        recyclerView.visibility = View.VISIBLE
                        layoutEmpty.visibility  = View.GONE
                    }
                } else {
                    showEmpty()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat data.", Toast.LENGTH_SHORT).show()
                showEmpty()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun checkinMember(idBookingGym: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.updatePresensiGym(idBookingGym)
                if (response.isSuccessful) {
                    adapter.markAsHadir(idBookingGym)
                    Toast.makeText(requireContext(), "Presensi berhasil dicatat.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal mencatat presensi.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEmpty() {
        layoutEmpty.visibility  = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}
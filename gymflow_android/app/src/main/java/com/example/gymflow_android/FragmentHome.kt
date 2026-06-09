package com.example.gymflow_android

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
import com.example.gymflow_android.adapters.ScheduleAdapter
import com.example.gymflow_android.api.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentHome : Fragment() {

    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutEmpty: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar           = view.findViewById(R.id.progressBar)
        layoutEmpty           = view.findViewById(R.id.layoutEmpty)
        scheduleRecyclerView  = view.findViewById(R.id.scheduleRecyclerView)

        scheduleAdapter = ScheduleAdapter()
        scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        scheduleRecyclerView.adapter = scheduleAdapter

        loadSchedule()
    }

    private fun loadSchedule() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        progressBar.visibility          = View.VISIBLE
        layoutEmpty.visibility          = View.GONE
        scheduleRecyclerView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getJadwalHarian(today)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.data
                    if (data.isEmpty()) {
                        layoutEmpty.visibility          = View.VISIBLE
                        scheduleRecyclerView.visibility = View.GONE
                    } else {
                        scheduleAdapter.setData(data)
                        scheduleRecyclerView.visibility = View.VISIBLE
                        layoutEmpty.visibility          = View.GONE
                    }
                } else {
                    showEmpty()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat jadwal.", Toast.LENGTH_SHORT).show()
                showEmpty()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showEmpty() {
        layoutEmpty.visibility          = View.VISIBLE
        scheduleRecyclerView.visibility = View.GONE
    }
}
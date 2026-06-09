package com.example.gymflow_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymflow_android.R
import com.example.gymflow_android.models.JadwalHarian
import com.google.android.material.chip.Chip

class ScheduleAdapter(
    private var scheduleList: List<JadwalHarian> = emptyList()
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = scheduleList[position]

        // format jam: "07:00:00" -> "07:00"
        holder.tvJam.text            = item.jam.substring(0, 5)
        holder.tvNamaKelas.text      = item.nama_kelas
        holder.tvNamaInstruktur.text = item.nama_instruktur

        if (!item.status.isNullOrEmpty()) {
            holder.chipStatus.visibility = View.VISIBLE
            holder.chipStatus.text       = "Izin"
        } else {
            holder.chipStatus.visibility = View.GONE
        }
    }

    override fun getItemCount() = scheduleList.size

    fun setData(data: List<JadwalHarian>) {
        scheduleList = data
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJam: TextView            = itemView.findViewById(R.id.tvJam)
        val tvNamaKelas: TextView      = itemView.findViewById(R.id.tvNamaKelas)
        val tvNamaInstruktur: TextView = itemView.findViewById(R.id.tvNamaInstruktur)
        val chipStatus: Chip           = itemView.findViewById(R.id.chipStatus)
    }
}
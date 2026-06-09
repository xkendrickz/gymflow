package com.example.gymflow_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymflow_android.R
import com.example.gymflow_android.models.PresensiGymItem
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class PresensiGymAdapter(
    private val onCheckin: (PresensiGymItem) -> Unit,
    initialData: List<PresensiGymItem> = emptyList()
) : RecyclerView.Adapter<PresensiGymAdapter.ViewHolder>() {

    private var list: List<PresensiGymItem> = initialData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_presensi_gym, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.tvNamaMember.text = data.nama_member
        holder.tvSlotWaktu.text  = data.slot_waktu.substring(0, 5)

        if (data.status == 1) {
            holder.btnCheckin.visibility  = View.GONE
            holder.chipHadir.visibility   = View.VISIBLE
        } else {
            holder.btnCheckin.visibility  = View.VISIBLE
            holder.chipHadir.visibility   = View.GONE
            holder.btnCheckin.setOnClickListener { onCheckin(data) }
        }
    }

    override fun getItemCount() = list.size

    fun setData(data: List<PresensiGymItem>) {
        list = data
        notifyDataSetChanged()
    }

    fun markAsHadir(id: Int) {
        val index = list.indexOfFirst { it.id_booking_gym == id }
        if (index != -1) {
            list = list.toMutableList().also {
                it[index] = it[index].copy(status = 1)
            }
            try {
                notifyItemChanged(index)
            } catch (_: Exception) {
                // RecyclerView not attached in unit tests - safe to ignore
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaMember: TextView    = itemView.findViewById(R.id.tvNamaMember)
        val tvSlotWaktu: TextView     = itemView.findViewById(R.id.tvSlotWaktu)
        val btnCheckin: MaterialButton = itemView.findViewById(R.id.btnCheckin)
        val chipHadir: Chip           = itemView.findViewById(R.id.chipHadir)
    }
}
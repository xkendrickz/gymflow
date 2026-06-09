package com.example.gymflow_android.models

data class IzinRequest(
    val id_instruktur: Int,
    val id_jadwal_harian: Int,
    val detail_izin: String,
    val tanggal_izin: String
)
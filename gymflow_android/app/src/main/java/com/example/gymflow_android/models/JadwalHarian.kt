package com.example.gymflow_android.models

data class JadwalHarian(
    val id_jadwal_harian: Int,
    val nama_kelas: String,
    val nama_instruktur: String,
    val jam: String,
    val hari: String,
    val tarif: Double,
    val status: String?
)

data class JadwalHarianListResponse(
    val data: List<JadwalHarian>
)

data class BookingKelas(
    val id_member: Int,
    val id_jadwal_harian: Int
)
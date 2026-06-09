package com.example.gymflow_android.models

data class PresensiGymItem(
    val id_booking_gym: Int,
    val nama_member: String,
    val tanggal: String,
    val slot_waktu: String,
    val status: Int
)

data class PresensiGymListResponse(
    val data: List<PresensiGymItem>
)
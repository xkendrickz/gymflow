package com.example.gymflow_android.models

data class BookingGym(
    val id_booking_gym: Int = 0,
    val id_member: Int,
    val tanggal: String,
    val slot_waktu: String,
    val status: Int = 0
)

data class BookingGymListResponse(
    val data: List<BookingGym>
)
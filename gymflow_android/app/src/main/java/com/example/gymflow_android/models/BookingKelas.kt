package com.example.gymflow_android.models

data class BookingKelasItem(
    val id_booking_kelas: Int?,
    val nama_member: String?,
    val nama_instruktur: String?,
    val nama_kelas: String?,
    val jenis: String?,
    val status: Int?
)

data class BookingKelasListResponse(
    val data: List<BookingKelasItem>
)
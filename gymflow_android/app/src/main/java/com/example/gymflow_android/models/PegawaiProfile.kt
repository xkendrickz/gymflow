package com.example.gymflow_android.models

data class PegawaiProfile(
    val id_pegawai: Int,
    val id_role: Int,
    val nama_pegawai: String,
    val tanggal_lahir: String?,
    val username: String
)

data class PegawaiProfileResponse(
    val data: PegawaiProfile
)
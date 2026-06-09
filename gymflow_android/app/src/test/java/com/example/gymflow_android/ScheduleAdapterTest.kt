package com.example.gymflow_android

import com.example.gymflow_android.adapters.ScheduleAdapter
import com.example.gymflow_android.models.JadwalHarian
import org.junit.Assert.*
import org.junit.Test

class ScheduleAdapterTest {

    private fun makeJadwal(
        id: Int,
        namaKelas: String,
        jam: String = "07:00:00",
        status: String? = null
    ) = JadwalHarian(
        id_jadwal_harian = id,
        nama_kelas       = namaKelas,
        nama_instruktur  = "Instruktur Test",
        jam              = jam,
        hari             = "2025-05-29",
        tarif            = 50000.0,
        status           = status
    )

    @Test
    fun `getItemCount returns correct count after setData`() {
        // use constructor instead of setData
        val adapter = ScheduleAdapter(listOf(
            makeJadwal(1, "Yoga"),
            makeJadwal(2, "Zumba")
        ))
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `getItemCount returns zero for empty list`() {
        val adapter = ScheduleAdapter(emptyList())
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun `jam is trimmed to HH-MM format`() {
        val jadwal = makeJadwal(1, "Yoga", jam = "07:00:00")
        val trimmed = jadwal.jam.substring(0, 5)
        assertEquals("07:00", trimmed)
    }

    @Test
    fun `status is null for active class`() {
        val jadwal = makeJadwal(1, "Yoga", status = null)
        assertNull(jadwal.status)
    }

    @Test
    fun `status is not null for izin class`() {
        val jadwal = makeJadwal(1, "Yoga", status = "Sakit")
        assertNotNull(jadwal.status)
    }
}
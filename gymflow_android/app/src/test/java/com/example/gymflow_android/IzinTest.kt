package com.example.gymflow_android

import com.example.gymflow_android.api.ApiService
import com.example.gymflow_android.models.IzinRequest
import com.example.gymflow_android.models.JadwalHarian
import com.example.gymflow_android.models.JadwalHarianListResponse
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class IzinTest {

    private lateinit var api: ApiService

    @Before
    fun setup() {
        api = mockk()
    }

    @Test
    fun `getJadwalHarianByInstruktur returns upcoming schedule`() = runTest {
        val jadwalList = listOf(
            JadwalHarian(id_jadwal_harian = 1, nama_kelas = "Yoga",
                nama_instruktur = "John", jam = "07:00:00",
                hari = "2025-06-02", tarif = 50000.0, status = null),
        )
        coEvery { api.getJadwalHarianByInstruktur(1) } returns Response.success(
            JadwalHarianListResponse(data = jadwalList)
        )

        val response = api.getJadwalHarianByInstruktur(1)

        assertTrue(response.isSuccessful)
        assertEquals(1, response.body()!!.data.size)
        assertEquals("Yoga", response.body()!!.data[0].nama_kelas)
    }

    @Test
    fun `createIzin submits correct payload`() = runTest {
        val request = IzinRequest(
            id_instruktur    = 1,
            id_jadwal_harian = 1,
            detail_izin      = "Sakit demam",
            tanggal_izin     = "2025-06-02"
        )
        coEvery { api.createIzin(request) } returns Response.success(Unit)

        val response = api.createIzin(request)

        assertTrue(response.isSuccessful)
        coVerify { api.createIzin(request) }
    }

    @Test
    fun `jadwal label is formatted correctly`() {
        val jadwal = JadwalHarian(
            id_jadwal_harian = 1,
            nama_kelas       = "Yoga",
            nama_instruktur  = "John",
            jam              = "07:00:00",
            hari             = "2025-06-02",
            tarif            = 50000.0,
            status           = null
        )

        val label = "${jadwal.nama_kelas} - ${jadwal.hari} ${jadwal.jam}"
        assertEquals("Yoga - 2025-06-02 07:00:00", label)
    }
}
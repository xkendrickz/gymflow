package com.example.gymflow_android

import com.example.gymflow_android.api.ApiService
import com.example.gymflow_android.models.BookingGym
import com.example.gymflow_android.models.BookingGymListResponse
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class BookingGymTest {

    private lateinit var api: ApiService

    @Before
    fun setup() {
        api = mockk()
    }

    @Test
    fun `today booking is found when it exists`() = runTest {
        val today = "2025-05-29"
        coEvery { api.getBookingGymByMember(1) } returns Response.success(
            BookingGymListResponse(data = listOf(
                BookingGym(id_booking_gym = 1, id_member = 1,
                    tanggal = today, slot_waktu = "07:00:00", status = 0),
                BookingGym(id_booking_gym = 2, id_member = 1,
                    tanggal = "2025-05-30", slot_waktu = "09:00:00", status = 0),
            ))
        )

        val response     = api.getBookingGymByMember(1)
        val todayBooking = response.body()!!.data.firstOrNull { it.tanggal == today }

        assertNotNull(todayBooking)
        assertEquals("07:00:00", todayBooking!!.slot_waktu)
        assertEquals(0, todayBooking.status)
    }

    @Test
    fun `today booking is null when no booking exists for today`() = runTest {
        val today = "2025-05-29"
        coEvery { api.getBookingGymByMember(1) } returns Response.success(
            BookingGymListResponse(data = listOf(
                BookingGym(id_booking_gym = 1, id_member = 1,
                    tanggal = "2025-05-30", slot_waktu = "07:00:00", status = 0),
            ))
        )

        val response     = api.getBookingGymByMember(1)
        val todayBooking = response.body()!!.data.firstOrNull { it.tanggal == today }

        assertNull(todayBooking)
    }

    @Test
    fun `checkin calls updatePresensiGym with correct id`() = runTest {
        coEvery { api.updatePresensiGym(5) } returns Response.success(Unit)

        val response = api.updatePresensiGym(5)

        assertTrue(response.isSuccessful)
        coVerify(exactly = 1) { api.updatePresensiGym(5) }
    }

    @Test
    fun `delete booking calls correct endpoint`() = runTest {
        coEvery { api.deleteBookingGym(1, "2025-05-29") } returns Response.success(Unit)

        val response = api.deleteBookingGym(1, "2025-05-29")

        assertTrue(response.isSuccessful)
        coVerify { api.deleteBookingGym(1, "2025-05-29") }
    }
}
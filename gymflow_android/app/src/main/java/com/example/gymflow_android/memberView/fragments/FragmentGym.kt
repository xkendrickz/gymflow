package com.example.gymflow_android.memberView.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gymflow_android.R
import com.example.gymflow_android.api.RetrofitClient
import com.example.gymflow_android.models.BookingGym
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentGym : Fragment() {

    companion object {
        private val SLOT_WAKTU = arrayOf(
            "07:00:00", "09:00:00", "11:00:00",
            "13:00:00", "15:00:00", "17:00:00", "19:00:00"
        )
    }

    private var etTanggal: EditText? = null
    private var edTanggal2: AutoCompleteTextView? = null
    private var edSlotWaktu: AutoCompleteTextView? = null

    // check-in views
    private var tvTodaySlot: TextView? = null
    private var tvNoBooking: TextView? = null
    private var btnCheckin: MaterialButton? = null
    private var chipSudahCheckin: Chip? = null

    private val myPreference = "myPref"
    private var userId: Int = -1
    private var todayBooking: BookingGym? = null
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gym, container, false)

        etTanggal      = view.findViewById(R.id.etTanggal)
        edTanggal2     = view.findViewById(R.id.edTanggal2)
        edSlotWaktu    = view.findViewById(R.id.edSlotWaktu)
        tvTodaySlot    = view.findViewById(R.id.tvTodaySlot)
        tvNoBooking    = view.findViewById(R.id.tvNoBooking)
        btnCheckin     = view.findViewById(R.id.btn_checkin)
        chipSudahCheckin = view.findViewById(R.id.chipSudahCheckin)

        userId = requireContext()
            .getSharedPreferences(myPreference, Context.MODE_PRIVATE)
            .getInt("userId", -1)

        edSlotWaktu?.setAdapter(ArrayAdapter(requireContext(), R.layout.item_list, SLOT_WAKTU))
        edSlotWaktu?.isFocusable = false
        etTanggal?.isFocusable = false
        etTanggal?.setOnClickListener { showDatePicker() }

        loadBookings()

        view.findViewById<MaterialButton>(R.id.btn_save).setOnClickListener { createBooking() }
        view.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            val tanggal = edTanggal2?.text.toString()
            if (tanggal.isNotEmpty()) deleteBooking(tanggal)
            else Toast.makeText(requireContext(), "Pilih tanggal terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
        btnCheckin?.setOnClickListener { checkInGym() }

        return view
    }

    private fun loadBookings() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getBookingGymByMember(userId)
                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.data

                    // populate cancel dropdown
                    val dates = bookings.map { it.tanggal }
                    edTanggal2?.setAdapter(ArrayAdapter(requireContext(), R.layout.item_list, dates))

                    // find today's booking
                    todayBooking = bookings.firstOrNull { it.tanggal == today }
                    updateCheckinSection()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCheckinSection() {
        val booking = todayBooking
        if (booking == null) {
            tvNoBooking?.visibility    = View.VISIBLE
            tvTodaySlot?.visibility    = View.GONE
            btnCheckin?.visibility     = View.GONE
            chipSudahCheckin?.visibility = View.GONE
        } else {
            tvNoBooking?.visibility = View.GONE
            tvTodaySlot?.visibility = View.VISIBLE
            tvTodaySlot?.text       = "Slot hari ini: ${booking.slot_waktu}"

            if (booking.status == 0) {
                btnCheckin?.visibility       = View.VISIBLE
                chipSudahCheckin?.visibility = View.GONE
            } else {
                btnCheckin?.visibility       = View.GONE
                chipSudahCheckin?.visibility = View.VISIBLE
            }
        }
    }

    private fun checkInGym() {
        val booking = todayBooking ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.updatePresensiGym(booking.id_booking_gym)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Check-in gym berhasil!", Toast.LENGTH_SHORT).show()
                    todayBooking = booking.copy(status = 1)
                    updateCheckinSection()
                } else {
                    Toast.makeText(requireContext(), "Gagal check-in.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBooking() {
        val tanggal   = etTanggal?.text.toString()
        val slotWaktu = edSlotWaktu?.text.toString()

        if (tanggal.isEmpty() || slotWaktu.isEmpty()) {
            Toast.makeText(requireContext(), "Tanggal dan slot waktu harus diisi.", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createBookingGym(
                    BookingGym(id_member = userId, tanggal = tanggal, slot_waktu = slotWaktu)
                )
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Booking berhasil ditambahkan.", Toast.LENGTH_SHORT).show()
                    etTanggal?.setText("")
                    edSlotWaktu?.setText("")
                    loadBookings()
                } else {
                    Toast.makeText(requireContext(), "Gagal membuat booking.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteBooking(tanggal: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.deleteBookingGym(userId, tanggal)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Booking berhasil dihapus.", Toast.LENGTH_SHORT).show()
                    loadBookings()
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus booking.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            cal.set(y, m, d)
            etTanggal?.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time))
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }
}
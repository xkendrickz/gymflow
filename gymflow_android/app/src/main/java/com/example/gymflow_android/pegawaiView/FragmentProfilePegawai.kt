package com.example.gymflow_android.pegawaiView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gymflow_android.MainActivity
import com.example.gymflow_android.R
import com.example.gymflow_android.SessionManager
import com.example.gymflow_android.api.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class FragmentProfilePegawai : Fragment() {

    private val myPreference = "myPref"
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_pegawai, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = requireContext()
            .getSharedPreferences(myPreference, Context.MODE_PRIVATE)
            .getInt("userId", -1)

        loadProfile(view)

        view.findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    private fun loadProfile(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getPegawaiProfile(userId)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.data
                    view.findViewById<TextView>(R.id.tvNama).text          = data.nama_pegawai
                    view.findViewById<TextView>(R.id.tvUsername).text      = data.username
                    view.findViewById<TextView>(R.id.tvTanggalLahir).text  = data.tanggal_lahir ?: "-"
                    view.findViewById<TextView>(R.id.tvRole).text          =
                        if (data.id_role == 1) "Admin" else "Kasir"
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat profil.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                RetrofitClient.api.logout()
            } catch (_: Exception) { }
            finally {
                SessionManager.redirectToLogin()
            }
        }
    }
}
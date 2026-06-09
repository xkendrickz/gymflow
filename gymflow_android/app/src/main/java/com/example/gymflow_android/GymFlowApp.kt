package com.example.gymflow_android

import android.app.Application
import com.example.gymflow_android.api.RetrofitClient

class GymFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(this)
        SessionManager.init(this)
    }
}
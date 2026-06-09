package com.example.gymflow_android

import android.content.Context
import android.content.Intent

object SessionManager {
    private const val PREF_NAME = "myPref"
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun clearSession() {
        appContext?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            ?.edit()
            ?.clear()
            ?.apply()
    }

    fun redirectToLogin() {
        appContext?.let { ctx ->
            clearSession()
            val intent = Intent(ctx, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            ctx.startActivity(intent)
        }
    }
}
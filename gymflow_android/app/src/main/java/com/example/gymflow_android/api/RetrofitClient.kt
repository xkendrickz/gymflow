package com.example.gymflow_android.api

import android.content.Context
import com.example.gymflow_android.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://gymflow-api-production.up.railway.app/api/"
    // for production: "https://gymflow-api-production.up.railway.app/api/"
    // for development: "https://192.168.x.x:8000/api/"
    private const val PREF_NAME = "myPref"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun authInterceptor() = Interceptor { chain ->
        val token = appContext
            ?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            ?.getString("token", null)

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        chain.proceed(request)
    }

    private fun unauthorizedInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            response.close()
            SessionManager.redirectToLogin()
        }

        response
    }

    private fun buildClient() = OkHttpClient.Builder()
        .addInterceptor(authInterceptor())
        .addInterceptor(unauthorizedInterceptor())
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
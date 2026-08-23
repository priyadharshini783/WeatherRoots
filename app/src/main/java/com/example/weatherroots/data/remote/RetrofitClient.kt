package com.example.weatherroots.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    // Physical Android phone using ADB reverse:
    // adb reverse tcp:8000 tcp:8000
    private const val BASE_URL =
        "http://127.0.0.1:8000/"


    // =========================================================
    // HTTP Logging
    // =========================================================

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {

            level =
                HttpLoggingInterceptor.Level.BODY
        }


    // =========================================================
    // OkHttp Client
    // =========================================================

    private val okHttpClient =
        OkHttpClient.Builder()

            // Connection from Android -> FastAPI
            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )

            // Wait for FastAPI/RAG/Gemini response
            .readTimeout(
                90,
                TimeUnit.SECONDS
            )

            // Upload request data
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )

            // Maximum duration for the complete API request
            .callTimeout(
                120,
                TimeUnit.SECONDS
            )

            .addInterceptor(
                loggingInterceptor
            )

            .build()


    // =========================================================
    // Retrofit
    // =========================================================

    val api: WeatherRootsApi by lazy {

        Retrofit.Builder()

            .baseUrl(
                BASE_URL
            )

            .client(
                okHttpClient
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

            .create(
                WeatherRootsApi::class.java
            )
    }
}
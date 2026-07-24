package com.example.weatherroots.di

import com.example.weatherroots.BuildConfig
import com.example.weatherroots.data.repository.WeatherRepositoryImpl
import com.example.weatherroots.domain.repository.WeatherRepository

object AppContainer {

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            weatherApiService = NetworkModule.weatherApiService,
            apiKey = BuildConfig.OPENWEATHER_API_KEY
        )
    }
}
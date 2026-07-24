package com.example.weatherroots.domain.repository

import com.example.weatherroots.domain.model.WeatherData

interface WeatherRepository {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherData
}
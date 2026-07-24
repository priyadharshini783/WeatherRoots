package com.example.weatherroots.data.repository

import com.example.weatherroots.data.remote.WeatherApiService
import com.example.weatherroots.domain.model.WeatherData
import com.example.weatherroots.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApiService: WeatherApiService,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherData {

        if (apiKey.isBlank()) {
            throw IllegalStateException(
                "OpenWeather API key is missing."
            )
        }

        val response = weatherApiService.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )

        val weatherCondition = response.weather.firstOrNull()

        return WeatherData(
            temperature = response.main.temp,
            humidity = response.main.humidity,

            // OpenWeather metric wind speed is m/s.
            // Our WeatherRoots UI displays km/h.
            windSpeed = response.wind.speed * 3.6,

            rainfall = response.rain?.oneHour ?: 0.0,

            conditionDescription =
                weatherCondition?.description ?: "Unknown",

            conditionIconCode =
                weatherCondition?.icon ?: "",

            locationName =
                "${response.name}, ${response.sys.country}"
        )
    }
}
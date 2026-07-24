package com.example.weatherroots.domain.model

data class WeatherData(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val rainfall: Double,
    val conditionDescription: String,
    val conditionIconCode: String,
    val locationName: String
)
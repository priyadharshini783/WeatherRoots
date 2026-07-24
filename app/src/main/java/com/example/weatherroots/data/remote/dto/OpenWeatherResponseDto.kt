package com.example.weatherroots.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherResponseDto(
    val weather: List<WeatherConditionDto> = emptyList(),
    val main: MainWeatherDto,
    val wind: WindDto,
    val rain: RainDto? = null,
    val sys: SystemDto,
    val name: String
)

@Serializable
data class WeatherConditionDto(
    val description: String,
    val icon: String
)

@Serializable
data class MainWeatherDto(
    val temp: Double,
    val humidity: Int
)

@Serializable
data class WindDto(
    val speed: Double
)

@Serializable
data class RainDto(
    @SerialName("1h")
    val oneHour: Double = 0.0
)

@Serializable
data class SystemDto(
    val country: String
)
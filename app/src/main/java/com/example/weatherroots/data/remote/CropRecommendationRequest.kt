package com.example.weatherroots.data.remote

data class CropRecommendationRequest(
    val temperature: Double,
    val humidity: Double,
    val rainfall: Double,
    val soil_type: String,
    val water_availability: String,
    val previous_crop: String,
    val season: String
)
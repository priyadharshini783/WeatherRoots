package com.example.weatherroots.data.remote

data class CropRecommendationResponse(
    val recommended_crop: String,
    val suitability_score: Double,
    val alternatives: List<AlternativeCrop>,
    val explanation: String,
    val current_rainfall: Double,
    val climate_rainfall: Double,
    val rainfall_source: String
)
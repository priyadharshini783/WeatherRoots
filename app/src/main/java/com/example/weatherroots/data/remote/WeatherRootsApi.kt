package com.example.weatherroots.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface WeatherRootsApi {

    // Crop Recommendation
    @POST("crop/recommend")
    suspend fun getCropRecommendation(
        @Body request: CropRecommendationRequest
    ): CropRecommendationResponse


    // Voice Assistant
    @POST("voice/query")
    suspend fun processVoiceQuery(
        @Body request: VoiceQueryRequest
    ): VoiceQueryResponse
}
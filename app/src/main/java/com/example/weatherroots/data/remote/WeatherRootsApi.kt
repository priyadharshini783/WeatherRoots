package com.example.weatherroots.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface WeatherRootsApi {

    @POST("crop/recommend")
    suspend fun getCropRecommendation(
        @Body request: CropRecommendationRequest
    ): CropRecommendationResponse
}
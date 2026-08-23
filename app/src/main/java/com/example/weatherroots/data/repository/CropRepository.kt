package com.example.weatherroots.data.repository

import com.example.weatherroots.data.remote.CropRecommendationRequest
import com.example.weatherroots.data.remote.CropRecommendationResponse
import com.example.weatherroots.data.remote.RetrofitClient

class CropRepository {

    suspend fun getCropRecommendation(
        request: CropRecommendationRequest
    ): Result<CropRecommendationResponse> {

        return try {

            val response =
                RetrofitClient.api.getCropRecommendation(request)

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
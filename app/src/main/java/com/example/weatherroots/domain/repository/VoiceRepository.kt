package com.example.weatherroots.data.repository

import com.example.weatherroots.data.remote.RetrofitClient
import com.example.weatherroots.data.remote.VoiceQueryRequest
import com.example.weatherroots.data.remote.VoiceQueryResponse

class VoiceRepository {

    suspend fun askFarmerQuestion(
        question: String
    ): Result<VoiceQueryResponse> {

        return try {

            val request =
                VoiceQueryRequest(
                    question = question
                )

            val response =
                RetrofitClient.api
                    .processVoiceQuery(
                        request
                    )

            Result.success(
                response
            )

        } catch (e: Exception) {

            Result.failure(
                e
            )
        }
    }
}
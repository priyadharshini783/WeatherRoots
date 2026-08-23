package com.example.weatherroots.data.remote

data class VoiceQueryResponse(
    val detected_language: String,
    val original_question: String,
    val english_question: String,
    val response: String
)
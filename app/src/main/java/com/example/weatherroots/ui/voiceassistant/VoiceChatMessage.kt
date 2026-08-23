package com.example.weatherroots.ui.voiceassistant


data class VoiceChatMessage(

    val id: Long,

    val text: String,

    val isUser: Boolean,

    val language: String,

    val timestamp: Long
)
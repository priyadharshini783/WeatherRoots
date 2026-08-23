package com.example.weatherroots.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "voice_messages"
)
data class VoiceMessageEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val isUser: Boolean,

    val language: String,

    val timestamp: Long =
        System.currentTimeMillis()
)
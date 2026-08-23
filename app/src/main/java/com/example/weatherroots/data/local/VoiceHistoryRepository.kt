package com.example.weatherroots.data.local

import kotlinx.coroutines.flow.Flow

class VoiceHistoryRepository(
    private val dao: VoiceMessageDao
) {

    val messages:
            Flow<List<VoiceMessageEntity>> =
        dao.getAllMessages()

    suspend fun saveUserMessage(
        text: String,
        language: String
    ) {

        dao.insertMessage(
            VoiceMessageEntity(
                text = text,
                isUser = true,
                language = language
            )
        )
    }

    suspend fun saveAiMessage(
        text: String,
        language: String
    ) {

        dao.insertMessage(
            VoiceMessageEntity(
                text = text,
                isUser = false,
                language = language
            )
        )
    }

    suspend fun clearHistory() {
        dao.clearAllMessages()
    }
}
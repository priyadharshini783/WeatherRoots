package com.example.weatherroots.ui.voiceassistant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.weatherroots.data.local.VoiceHistoryRepository
import com.example.weatherroots.data.local.WeatherRootsDatabase

import com.example.weatherroots.data.remote.VoiceQueryResponse
import com.example.weatherroots.data.repository.VoiceRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch


class VoiceAssistantViewModel(
    application: Application
) : AndroidViewModel(application) {


    // =========================================================
    // FASTAPI REPOSITORY
    // =========================================================

    private val repository =
        VoiceRepository()


    // =========================================================
    // ROOM DATABASE
    // =========================================================

    private val database =
        WeatherRootsDatabase.getDatabase(
            application
        )


    private val historyRepository =
        VoiceHistoryRepository(
            database.voiceMessageDao()
        )


    // =========================================================
    // CONVERSATION HISTORY
    // =========================================================

    val messages:
            StateFlow<List<VoiceChatMessage>> =

        historyRepository
            .messages
            .map { entities ->

                entities.map { entity ->

                    VoiceChatMessage(

                        id =
                            entity.id,

                        text =
                            entity.text,

                        isUser =
                            entity.isUser,

                        language =
                            entity.language,

                        timestamp =
                            entity.timestamp
                    )
                }
            }
            .stateIn(

                scope =
                    viewModelScope,

                started =
                    SharingStarted.WhileSubscribed(
                        5_000
                    ),

                initialValue =
                    emptyList()
            )


    // =========================================================
    // LOADING STATE
    // =========================================================

    private val _isLoading =
        MutableStateFlow(false)


    val isLoading:
            StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // =========================================================
    // LATEST BACKEND RESPONSE
    //
    // Keeping this so your current VoiceAssistantScreen
    // can continue using viewModel.response.
    // =========================================================

    private val _response =
        MutableStateFlow<
                VoiceQueryResponse?
                >(null)


    val response:
            StateFlow<
                    VoiceQueryResponse?
                    > =
        _response.asStateFlow()


    // =========================================================
    // ERROR STATE
    // =========================================================

    private val _errorMessage =
        MutableStateFlow<String?>(
            null
        )


    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()


    // =========================================================
    // ASK WEATHERROOTS AI
    // =========================================================

    fun askQuestion(

        question: String,

        selectedLanguage: String =
            "English"

    ) {

        val cleanedQuestion =
            question.trim()


        if (cleanedQuestion.isBlank()) {

            _errorMessage.value =
                "Please speak or enter a farming question."

            return
        }


        viewModelScope.launch {

            try {

                // ---------------------------------------------
                // Start loading
                // ---------------------------------------------

                _isLoading.value =
                    true

                _errorMessage.value =
                    null


                // ---------------------------------------------
                // Convert selected language to language code
                // ---------------------------------------------

                val languageCode =
                    languageNameToCode(
                        selectedLanguage
                    )


                // ---------------------------------------------
                // STEP 1
                // Save farmer question in Room
                // ---------------------------------------------

                historyRepository
                    .saveUserMessage(

                        text =
                            cleanedQuestion,

                        language =
                            languageCode
                    )


                // ---------------------------------------------
                // STEP 2
                // Call FastAPI Voice Assistant
                // ---------------------------------------------

                repository
                    .askFarmerQuestion(
                        cleanedQuestion
                    )
                    .onSuccess { result ->


                        // -------------------------------------
                        // Keep latest response for current UI
                        // -------------------------------------

                        _response.value =
                            result


                        // -------------------------------------
                        // STEP 3
                        // Save AI response in Room
                        // -------------------------------------

                        if (
                            result.response
                                .isNotBlank()
                        ) {

                            historyRepository
                                .saveAiMessage(

                                    text =
                                        result.response,

                                    language =
                                        result.detected_language
                                )
                        }
                    }
                    .onFailure { error ->

                        _errorMessage.value =

                            error.message

                                ?: "Unable to contact WeatherRoots AI."
                    }


            } catch (error: Exception) {

                _errorMessage.value =

                    error.message

                        ?: "Something went wrong while processing your question."


            } finally {

                _isLoading.value =
                    false
            }
        }
    }


    // =========================================================
    // CLEAR ONLY CURRENT RESPONSE
    // =========================================================

    fun clearResponse() {

        _response.value =
            null

        _errorMessage.value =
            null
    }


    // =========================================================
    // CLEAR COMPLETE SAVED CONVERSATION
    // =========================================================

    fun clearConversation() {

        viewModelScope.launch {

            try {

                historyRepository
                    .clearHistory()

                _response.value =
                    null

                _errorMessage.value =
                    null

            } catch (error: Exception) {

                _errorMessage.value =
                    "Unable to clear conversation history."
            }
        }
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _errorMessage.value =
            null
    }


    // =========================================================
    // LANGUAGE NAME -> LANGUAGE CODE
    // =========================================================

    private fun languageNameToCode(
        language: String
    ): String {

        return when (
            language
                .trim()
                .lowercase()
        ) {

            "tamil" ->
                "ta"

            "hindi" ->
                "hi"

            "telugu" ->
                "te"

            "kannada" ->
                "kn"

            "english" ->
                "en"

            else ->
                "en"
        }
    }
}
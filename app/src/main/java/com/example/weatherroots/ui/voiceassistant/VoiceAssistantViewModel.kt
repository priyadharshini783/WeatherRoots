package com.example.weatherroots.ui.voiceassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroots.data.remote.VoiceQueryResponse
import com.example.weatherroots.data.repository.VoiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoiceAssistantViewModel : ViewModel() {

    private val repository =
        VoiceRepository()


    private val _isLoading =
        MutableStateFlow(false)

    val isLoading:
            StateFlow<Boolean> =
        _isLoading.asStateFlow()


    private val _response =
        MutableStateFlow<
                VoiceQueryResponse?
                >(null)

    val response:
            StateFlow<
                    VoiceQueryResponse?
                    > =
        _response.asStateFlow()


    private val _errorMessage =
        MutableStateFlow<String?>(
            null
        )

    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()


    fun askQuestion(
        question: String
    ) {

        if (question.isBlank()) {

            _errorMessage.value =
                "Please speak or enter a farming question."

            return
        }


        viewModelScope.launch {

            _isLoading.value =
                true

            _errorMessage.value =
                null


            repository
                .askFarmerQuestion(
                    question
                )
                .onSuccess {

                    _response.value =
                        it
                }
                .onFailure {

                    _errorMessage.value =
                        it.message
                            ?: "Unable to contact WeatherRoots AI."
                }


            _isLoading.value =
                false
        }
    }


    fun clearResponse() {

        _response.value =
            null

        _errorMessage.value =
            null
    }
}
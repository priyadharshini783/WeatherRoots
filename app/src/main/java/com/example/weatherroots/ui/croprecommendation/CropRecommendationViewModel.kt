package com.example.weatherroots.ui.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroots.data.remote.CropRecommendationRequest
import com.example.weatherroots.data.remote.CropRecommendationResponse
import com.example.weatherroots.data.repository.CropRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CropRecommendationViewModel : ViewModel() {

    private val repository =
        CropRepository()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _recommendation =
        MutableStateFlow<CropRecommendationResponse?>(null)

    val recommendation:
            StateFlow<CropRecommendationResponse?> =
        _recommendation.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    fun getRecommendation(
        temperature: Double,
        humidity: Double,
        rainfall: Double,
        soilType: String,
        waterAvailability: String,
        previousCrop: String,
        season: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null
            _recommendation.value = null

            val request =
                CropRecommendationRequest(
                    temperature = temperature,
                    humidity = humidity,
                    rainfall = rainfall,
                    soil_type = soilType,
                    water_availability =
                        waterAvailability,
                    previous_crop =
                        previousCrop,
                    season = season
                )

            repository
                .getCropRecommendation(request)
                .onSuccess {

                    _recommendation.value =
                        it
                }
                .onFailure {

                    _errorMessage.value =
                        it.message
                            ?: "Unable to get crop recommendation."
                }

            _isLoading.value = false
        }
    }

    fun clearResult() {

        _recommendation.value = null
        _errorMessage.value = null
    }
}
package com.example.weatherroots.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroots.domain.repository.WeatherRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherDashboardViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        fetchWeatherData()
    }

    fun fetchWeatherData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading

            try {
                val weatherData = weatherRepository.getWeather(
                    latitude = 13.0827,
                    longitude = 80.2707
                )

                _uiState.value =
                    DashboardUiState.Success(weatherData)

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                _uiState.value = DashboardUiState.Error(
                    message = exception.message
                        ?: "Unable to load weather data."
                )
            }
        }
    }
}
package com.example.weatherroots.ui.dashboard

import com.example.weatherroots.domain.model.WeatherData

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val weatherData: WeatherData) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
package com.example.weatherroots.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherroots.domain.repository.WeatherRepository

class WeatherDashboardViewModelFactory(
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                WeatherDashboardViewModel::class.java
            )
        ) {
            return WeatherDashboardViewModel(
                weatherRepository = weatherRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
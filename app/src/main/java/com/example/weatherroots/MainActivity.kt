package com.example.weatherroots
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherroots.di.AppContainer
import com.example.weatherroots.ui.DashboardRoute
import com.example.weatherroots.ui.RecommendationRoute
import com.example.weatherroots.ui.SettingsRoute
import com.example.weatherroots.ui.dashboard.DashboardUiState
import com.example.weatherroots.ui.dashboard.WeatherDashboardRoute
import com.example.weatherroots.ui.dashboard.WeatherDashboardViewModel
import com.example.weatherroots.ui.dashboard.WeatherDashboardViewModelFactory
import com.example.weatherroots.ui.recommendation.CropRecommendationScreen
import com.example.weatherroots.ui.settings.SettingsScreen
import com.example.weatherroots.ui.theme.WeatherRootsTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            WeatherRootsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController =
                        rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = DashboardRoute
                    ) {

                        // -----------------------------
                        // Dashboard
                        // -----------------------------
                        composable<DashboardRoute> {

                            val dashboardViewModel:
                                    WeatherDashboardViewModel =
                                viewModel(
                                    factory =
                                        WeatherDashboardViewModelFactory(
                                            weatherRepository =
                                                AppContainer.weatherRepository
                                        )
                                )

                            WeatherDashboardRoute(
                                viewModel = dashboardViewModel,

                                onNavigateToRecommendation = {

                                    val currentState =
                                        dashboardViewModel.uiState.value

                                    if (currentState is DashboardUiState.Success) {

                                        navController.navigate(
                                            RecommendationRoute(
                                                temperature =
                                                    currentState.weatherData.temperature.toFloat(),

                                                humidity =
                                                    currentState.weatherData.humidity,

                                                rainfall =
                                                    currentState.weatherData.rainfall.toFloat()
                                            )
                                        )
                                    }
                                },

                                onNavigateToSettings = {
                                    navController.navigate(
                                        SettingsRoute
                                    )
                                }
                            )
                        }


                        // -----------------------------
                        // Crop Recommendation
                        // -----------------------------
                        composable<RecommendationRoute> {
                                backStackEntry ->

                            val args =
                                backStackEntry
                                    .toRoute<
                                            RecommendationRoute
                                            >()

                            CropRecommendationScreen(
                                args = args,

                                onNavigateBack = {
                                    navController
                                        .popBackStack()
                                }
                            )
                        }


                        // -----------------------------
                        // Settings
                        // -----------------------------
                        composable<SettingsRoute> {

                            SettingsScreen(
                                onNavigateBack = {
                                    navController
                                        .popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
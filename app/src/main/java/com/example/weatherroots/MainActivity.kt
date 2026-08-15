package com.example.weatherroots
import android.os.Bundle
import com.example.weatherroots.ui.ProfileRoute
import com.example.weatherroots.ui.profile.FarmerProfileScreen
import com.example.weatherroots.ui.SplashRoute
import com.example.weatherroots.ui.auth.SplashScreen
import com.example.weatherroots.ui.auth.LoginScreen
import com.example.weatherroots.ui.auth.AuthViewModel
import com.example.weatherroots.ui.auth.AuthViewModelFactory
import com.example.weatherroots.data.repository.AuthRepository
import com.example.weatherroots.data.remote.FirebaseAuthService
import com.example.weatherroots.ui.LoginRoute
import com.example.weatherroots.ui.auth.SignupScreen
import com.example.weatherroots.ui.SignupRoute
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.weatherroots.data.repository.FarmerProfileRepository
import com.example.weatherroots.ui.profile.FarmerProfileViewModel
import com.example.weatherroots.ui.profile.FarmerProfileViewModelFactory
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
import com.example.weatherroots.ui.VoiceAssistantRoute
import com.example.weatherroots.ui.voiceassistant.VoiceAssistantScreen

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
                        startDestination = SplashRoute
                    ) {
                        composable<SplashRoute> {


                            SplashScreen(

                                onUserLoggedIn = {

                                    navController.navigate(
                                        DashboardRoute
                                    ){

                                        popUpTo(SplashRoute){

                                            inclusive = true

                                        }

                                    }

                                },


                                onUserNotLoggedIn = {


                                    navController.navigate(
                                        LoginRoute
                                    ){

                                        popUpTo(SplashRoute){

                                            inclusive = true

                                        }

                                    }

                                }

                            )


                        }


                        // -----------------------------
                        // Dashboard
                        // -----------------------------
                        composable<LoginRoute> {


                            val authViewModel: AuthViewModel = viewModel(
                                factory = AuthViewModelFactory(
                                    repository = AuthRepository(
                                        FirebaseAuthService()
                                    )
                                )
                            )


                            LoginScreen(

                                viewModel = authViewModel,


                                onLoginSuccess = {

                                    navController.navigate(
                                        DashboardRoute
                                    ){

                                        popUpTo(LoginRoute){

                                            this.inclusive = true

                                        }

                                    }

                                },


                                onNavigateToSignup = {

                                    navController.navigate(
                                        SignupRoute
                                    )

                                }

                            )

                        }
                        composable<VoiceAssistantRoute> {


                            VoiceAssistantScreen(

                                onNavigateBack = {

                                    navController.popBackStack()

                                }

                            )

                        }
                        composable<SignupRoute> {


                            val authViewModel: AuthViewModel = viewModel(
                                factory = AuthViewModelFactory(
                                    AuthRepository(
                                        FirebaseAuthService()
                                    )
                                )
                            )


                            SignupScreen(

                                viewModel = authViewModel,


                                onSignupSuccess = {

                                    navController.navigate(
                                        DashboardRoute
                                    ){

                                        popUpTo(SignupRoute){

                                            this.inclusive = true

                                        }

                                    }

                                },


                                onNavigateToLogin = {

                                    navController.popBackStack()

                                }

                            )

                        }
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
                                },
                                onNavigateToVoiceAssistant = {

                                    navController.navigate(
                                        VoiceAssistantRoute
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
                        composable<ProfileRoute> {


                            val profileViewModel: FarmerProfileViewModel =
                                viewModel(
                                    factory = FarmerProfileViewModelFactory(
                                        FarmerProfileRepository()
                                    )
                                )


                            FarmerProfileScreen(

                                viewModel = profileViewModel,


                                onProfileSaved = {


                                    navController.navigate(
                                        DashboardRoute
                                    ){

                                        popUpTo(ProfileRoute){

                                            inclusive = true

                                        }

                                    }


                                }

                            )

                        }

                        // -----------------------------
                        // Settings
                        // -----------------------------
// Settings
// -----------------------------
                        composable<SettingsRoute> {

                            SettingsScreen(

                                onNavigateBack = {

                                    navController.popBackStack()

                                },


                                onLogout = {

                                    navController.navigate(LoginRoute) {

                                        popUpTo(0)

                                    }

                                },


                                onOpenProfile = {

                                    navController.navigate(ProfileRoute)

                                }

                            )

                        }

                    }
                }
            }
        }
    }
}
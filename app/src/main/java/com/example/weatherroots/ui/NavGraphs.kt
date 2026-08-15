package com.example.weatherroots.ui

import kotlinx.serialization.Serializable


// Route 0: Authentication

@Serializable
object LoginRoute
@Serializable
object ProfileRoute
@Serializable
object VoiceAssistantRoute
@Serializable
object SplashRoute
@Serializable
object SignupRoute



// Route 1: Dashboard

@Serializable
object DashboardRoute



// Route 2: Crop Recommendation

@Serializable
data class RecommendationRoute(

    val temperature: Float,

    val humidity: Int,

    val rainfall: Float

)



// Route 3: Settings

@Serializable
object SettingsRoute
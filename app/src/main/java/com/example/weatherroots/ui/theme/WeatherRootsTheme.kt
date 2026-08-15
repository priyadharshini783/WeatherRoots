package com.example.weatherroots.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



private val LightColors = lightColorScheme(


    primary = Color(0xFF2E7D32),

    secondary = Color(0xFF81C784),

    tertiary = Color(0xFF0288D1),


    background = Color(0xFFF5FFF5),


    surface = Color.White,


    onPrimary = Color.White,


    onBackground = Color(0xFF1B1B1B)


)





@Composable
fun WeatherRootsTheme(

    content: @Composable () -> Unit

){

    MaterialTheme(

        colorScheme = LightColors,

        typography = Typography(),

        content = content

    )

}
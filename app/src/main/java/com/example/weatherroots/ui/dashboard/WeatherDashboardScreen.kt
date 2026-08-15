package com.example.weatherroots.ui.dashboard
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherroots.domain.model.WeatherData



import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDashboardRoute(


    viewModel: WeatherDashboardViewModel,


    onNavigateToRecommendation: () -> Unit,


    onNavigateToSettings: () -> Unit,


    onNavigateToVoiceAssistant: () -> Unit,


    modifier: Modifier = Modifier


){



    val uiState by viewModel.uiState.collectAsState()





    when(val state = uiState){



        is DashboardUiState.Loading -> {


            LoadingScreen(

                modifier

            )


        }





        is DashboardUiState.Success -> {


            WeatherDashboardScreen(


                weatherData = state.weatherData,


                onNavigateToRecommendation =
                    onNavigateToRecommendation,


                onNavigateToSettings =
                    onNavigateToSettings,


                onNavigateToVoiceAssistant =
                    onNavigateToVoiceAssistant,


                modifier = modifier


            )


        }





        is DashboardUiState.Error -> {



            ErrorScreen(


                message = state.message,


                onRetry = viewModel::fetchWeatherData,


                modifier = modifier


            )


        }



    }


}









@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDashboardScreen(


    weatherData: WeatherData,


    onNavigateToRecommendation: () -> Unit,


    onNavigateToSettings: () -> Unit,


    onNavigateToVoiceAssistant: () -> Unit,


    modifier: Modifier = Modifier


){





    Scaffold(



        modifier = modifier,



        containerColor = Color(0xFFF4FFF5),





        topBar = {



            CenterAlignedTopAppBar(



                title = {



                    Text(



                        text = "🌱 WeatherRoots",



                        fontSize = 22.sp,



                        fontWeight = FontWeight.Bold



                    )



                },





                actions = {



                    IconButton(



                        onClick = onNavigateToSettings



                    ){



                        Icon(



                            imageVector =
                                Icons.Default.Settings,


                            contentDescription =
                                "Settings"



                        )



                    }



                },





                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(



                        containerColor =
                            Color(0xFFF4FFF5)



                    )



            )



        }




    ){ innerPadding ->






        Column(



            modifier = Modifier



                .fillMaxSize()



                .padding(innerPadding)



                .verticalScroll(
                    rememberScrollState()
                )



                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),




            verticalArrangement =
                Arrangement.spacedBy(18.dp)



        ){







            // GREETING SECTION



            Column {



                Text(



                    text = "Good Morning Farmer 👨‍🌾",



                    fontSize = 24.sp,



                    fontWeight = FontWeight.Bold,



                    color = Color(0xFF1B5E20)



                )





                Spacer(

                    Modifier.height(5.dp)

                )





                Text(



                    text =
                        "Here is your farming weather update",



                    fontSize = 15.sp,



                    color =
                        Color.DarkGray



                )



            }








            // WEATHER HERO CARD


            PremiumWeatherHeroCard(


                weatherData = weatherData


            )








            Text(



                text = "Today's Conditions",



                fontSize = 20.sp,



                fontWeight = FontWeight.Bold



            )









            Row(



                modifier =
                    Modifier.fillMaxWidth(),



                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)



            ){



                ModernMetricCard(



                    emoji = "💧",



                    title = "Humidity",



                    value =
                        "${weatherData.humidity}%",



                    modifier =
                        Modifier.weight(1f)



                )







                ModernMetricCard(



                    emoji = "💨",



                    title = "Wind",



                    value =
                        "${weatherData.windSpeed.formatOneDecimal()} km/h",



                    modifier =
                        Modifier.weight(1f)



                )



            }









            Row(



                modifier =
                    Modifier.fillMaxWidth(),



                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)



            ){



                ModernMetricCard(



                    emoji = "🌧️",



                    title = "Rainfall",



                    value =
                        "${weatherData.rainfall.formatOneDecimal()} mm",



                    modifier =
                        Modifier.weight(1f)



                )







                ModernMetricCard(



                    emoji = "🌱",



                    title = "Crop Status",



                    value = "Good",



                    modifier =
                        Modifier.weight(1f)



                )



            }









            // FARMING INSIGHT



            FarmingInsightCard(



                weatherData = weatherData



            )









            Text(



                text = "Quick Actions",



                fontSize = 20.sp,



                fontWeight = FontWeight.Bold



            )









            Row(



                modifier =
                    Modifier.fillMaxWidth(),



                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)



            ){



                QuickActionCard(



                    emoji = "🎤",



                    title = "AI Assistant",



                    onClick =
                        onNavigateToVoiceAssistant,



                    modifier =
                        Modifier.weight(1f)



                )








                QuickActionCard(



                    emoji = "🌱",



                    title = "Crop Advice",



                    onClick =
                        onNavigateToRecommendation,



                    modifier =
                        Modifier.weight(1f)



                )



            }









            Spacer(

                Modifier.height(10.dp)

            )



        }



    }


}
@Composable
fun PremiumWeatherHeroCard(

    weatherData: WeatherData

) {


    Card(

        modifier = Modifier

            .fillMaxWidth()

            .height(230.dp),


        shape = RoundedCornerShape(30.dp),


        colors = CardDefaults.cardColors(

            containerColor = Color(0xFF2E7D32)

        )


    ){


        Column(


            modifier = Modifier

                .fillMaxSize()

                .padding(25.dp)


        ){



            Text(


                text = "📍 ${weatherData.locationName}",


                color = Color.White,


                fontSize = 16.sp,


                fontWeight = FontWeight.Medium



            )





            Spacer(

                Modifier.height(15.dp)

            )





            Row(


                modifier =
                    Modifier.fillMaxWidth(),


                verticalAlignment =
                    Alignment.CenterVertically,


                horizontalArrangement =
                    Arrangement.SpaceBetween



            ){



                Column {



                    Text(



                        text =
                            "${weatherData.temperature.formatOneDecimal()}°C",



                        color =
                            Color.White,



                        fontSize =
                            55.sp,



                        fontWeight =
                            FontWeight.Bold



                    )







                    Text(



                        text =
                            weatherData.conditionDescription
                                .replaceFirstChar {
                                    it.uppercase()
                                },



                        color =
                            Color.White,



                        fontSize =
                            18.sp



                    )


                }







                Text(



                    text =
                        getWeatherEmoji(
                            weatherData.conditionIconCode
                        ),



                    fontSize =
                        70.sp



                )



            }







            Spacer(

                Modifier.height(15.dp)

            )








            Surface(



                color =
                    Color.White.copy(
                        alpha = 0.25f
                    ),



                shape =
                    RoundedCornerShape(50.dp)



            ){



                Text(



                    text =
                        "🟢 Live Weather Update",



                    color =
                        Color.White,



                    modifier =
                        Modifier.padding(

                            horizontal = 15.dp,

                            vertical = 8.dp

                        ),



                    fontSize =
                        14.sp



                )



            }



        }



    }


}
@Composable
fun ModernMetricCard(

    emoji: String,

    title: String,

    value: String,

    modifier: Modifier = Modifier


){



    Card(



        modifier = modifier

            .height(120.dp),



        shape =
            RoundedCornerShape(22.dp),



        colors =
            CardDefaults.cardColors(



                containerColor =
                    Color.White



            ),



        elevation =
            CardDefaults.cardElevation(

                defaultElevation = 4.dp

            )


    ){



        Column(



            modifier =
                Modifier

                    .fillMaxSize()

                    .padding(16.dp),



            verticalArrangement =
                Arrangement.Center



        ){



            Text(



                text =
                    emoji,



                fontSize =
                    28.sp



            )





            Spacer(

                Modifier.height(5.dp)

            )





            Text(



                text =
                    title,



                fontSize =
                    13.sp,



                color =
                    Color.Gray



            )






            Text(



                text =
                    value,



                fontSize =
                    18.sp,



                fontWeight =
                    FontWeight.Bold,



                color =
                    Color(0xFF1B5E20)



            )



        }


    }


}
@Composable
fun QuickActionCard(

    emoji: String,

    title: String,

    onClick: () -> Unit,

    modifier: Modifier = Modifier


){



    Card(



        modifier = modifier

            .height(130.dp)

            .clickable {

                onClick()

            },



        shape =
            RoundedCornerShape(25.dp),



        colors =
            CardDefaults.cardColors(


                containerColor =
                    Color(0xFFE8F5E9)


            ),



        elevation =
            CardDefaults.cardElevation(

                defaultElevation = 3.dp

            )


    ){



        Column(



            modifier =
                Modifier

                    .fillMaxSize()

                    .padding(15.dp),



            horizontalAlignment =
                Alignment.CenterHorizontally,



            verticalArrangement =
                Arrangement.Center



        ){



            Text(



                text =
                    emoji,



                fontSize =
                    35.sp



            )





            Spacer(

                Modifier.height(8.dp)

            )





            Text(



                text =
                    title,



                fontSize =
                    15.sp,



                fontWeight =
                    FontWeight.Bold



            )



        }



    }


}
fun getWeatherEmoji(
    iconCode: String
): String {


    return when(iconCode){


        "01d",
        "01n" -> "☀️"


        "02d",
        "02n" -> "🌤️"


        "03d",
        "03n" -> "☁️"


        "04d",
        "04n" -> "☁️"


        "09d",
        "09n" -> "🌧️"


        "10d",
        "10n" -> "🌦️"


        "11d",
        "11n" -> "⛈️"


        "13d",
        "13n" -> "❄️"


        else -> "🌍"

    }

}
fun Double.formatOneDecimal(): String {

    return String.format(
        Locale.US,
        "%.1f",
        this
    )

}
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        CircularProgressIndicator(
            color = Color(0xFF2E7D32)
        )

    }

}
@Composable
fun ErrorScreen(
    message:String,
    onRetry:()->Unit,
    modifier: Modifier = Modifier
){

    Column(

        modifier = modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){

        Text(

            text = "⚠️ Something went wrong",

            fontSize = 22.sp,

            fontWeight = FontWeight.Bold

        )


        Spacer(
            Modifier.height(10.dp)
        )


        Text(
            text = message
        )


        Spacer(
            Modifier.height(20.dp)
        )


        Button(
            onClick = onRetry
        ){

            Text("Retry")

        }


    }

}
@Composable
fun FarmingInsightCard(
    weatherData: WeatherData
){

    Card(

        modifier = Modifier
            .fillMaxWidth(),

        shape = RoundedCornerShape(25.dp),

        colors = CardDefaults.cardColors(

            containerColor =
                Color(0xFFE8F5E9)

        )

    ){


        Column(

            modifier =
                Modifier.padding(20.dp)

        ){


            Text(

                text = "🌱 Farming Insight",

                fontSize = 20.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFF1B5E20)

            )


            Spacer(
                Modifier.height(10.dp)
            )


            Text(

                text =
                    when {

                        weatherData.rainfall > 5 ->
                            "Rain expected. Reduce irrigation."

                        weatherData.temperature > 35 ->
                            "High temperature. Protect crops."

                        else ->
                            "Weather conditions are good for farming."

                    }

            )


        }


    }

}
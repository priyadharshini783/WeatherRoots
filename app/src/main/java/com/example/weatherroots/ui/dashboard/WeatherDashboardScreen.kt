package com.example.weatherroots.ui.dashboard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherroots.domain.model.WeatherData
import java.util.Locale


@Composable

fun WeatherDashboardRoute(
    viewModel: WeatherDashboardViewModel,
    onNavigateToRecommendation: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {

        is DashboardUiState.Loading -> {
            LoadingScreen(
                modifier = modifier
            )
        }

        is DashboardUiState.Success -> {
            WeatherDashboardScreen(
                weatherData = state.weatherData,
                onNavigateToRecommendation = onNavigateToRecommendation,
                onNavigateToSettings = onNavigateToSettings,
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
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "WeatherRoots",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            WeatherHeroCard(
                weatherData = weatherData
            )

            Text(
                text = "Today's Conditions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                WeatherMetricItem(
                    emoji = "💧",
                    label = "Humidity",
                    value = "${weatherData.humidity}%",
                    modifier = Modifier.weight(1f)
                )

                WeatherMetricItem(
                    emoji = "💨",
                    label = "Wind",
                    value = "${weatherData.windSpeed.formatOneDecimal()} km/h",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                WeatherMetricItem(
                    emoji = "🌧️",
                    label = "Rainfall",
                    value = "${weatherData.rainfall.formatOneDecimal()} mm",
                    modifier = Modifier.weight(1f)
                )

                WeatherMetricItem(
                    emoji = "✅",
                    label = "Alerts",
                    value = "None",
                    modifier = Modifier.weight(1f)
                )
            }

            FarmingInsightCard(
                weatherData = weatherData
            )

            Button(
                onClick = onNavigateToRecommendation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "🌱",
                    fontSize = 20.sp
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Get Crop Recommendation",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun WeatherHeroCard(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(28.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "📍 ${weatherData.locationName}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "${weatherData.temperature.formatOneDecimal()}°C",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = weatherData.conditionDescription
                            .replaceFirstChar {
                                it.uppercase()
                            },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = getWeatherEmoji(
                        weatherData.conditionIconCode
                    ),
                    fontSize = 68.sp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Surface(
                color = MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.20f
                ),
                shape = RoundedCornerShape(50.dp)
            ) {

                Text(
                    text = "● Live weather",
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 7.dp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
private fun WeatherMetricItem(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = emoji,
                fontSize = 25.sp
            )

            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun FarmingInsightCard(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "🌾 Farming Weather Insight",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = getFarmingInsight(
                    weatherData = weatherData
                ),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CircularProgressIndicator()

            Text(
                text = "Fetching live weather...",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Unable to load weather",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text(
                text = "Retry"
            )
        }
    }
}


private fun getWeatherEmoji(
    iconCode: String
): String {

    return when {

        iconCode.startsWith("01") -> "☀️"

        iconCode.startsWith("02") -> "🌤️"

        iconCode.startsWith("03") ||
                iconCode.startsWith("04") -> "☁️"

        iconCode.startsWith("09") -> "🌧️"

        iconCode.startsWith("10") -> "🌦️"

        iconCode.startsWith("11") -> "⛈️"

        iconCode.startsWith("13") -> "❄️"

        iconCode.startsWith("50") -> "🌫️"

        else -> "🌤️"
    }
}


private fun getFarmingInsight(
    weatherData: WeatherData
): String {

    return when {

        weatherData.rainfall > 10 -> {
            "Heavy rainfall conditions detected. Avoid unnecessary irrigation and monitor fields for possible waterlogging."
        }

        weatherData.temperature > 35 -> {
            "High temperature conditions detected. Consider irrigation during cooler hours to reduce heat stress on crops."
        }

        weatherData.humidity > 85 -> {
            "High humidity may increase the risk of fungal diseases. Monitor crops carefully for early signs of infection."
        }

        else -> {
            "Current weather conditions appear generally suitable for normal farming activities."
        }
    }
}


private fun Double.formatOneDecimal(): String {
    return String.format(
        Locale.getDefault(),
        "%.1f",
        this
    )
}
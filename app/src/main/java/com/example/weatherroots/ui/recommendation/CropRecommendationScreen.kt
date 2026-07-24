package com.example.weatherroots.ui.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherroots.ui.RecommendationRoute
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropRecommendationScreen(
    args: RecommendationRoute,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nitrogen by remember {
        mutableStateOf("")
    }

    var phosphorus by remember {
        mutableStateOf("")
    }

    var potassium by remember {
        mutableStateOf("")
    }

    var phLevel by remember {
        mutableStateOf("")
    }

    var showResult by remember {
        mutableStateOf(false)
    }

    val nitrogenValue = nitrogen.toDoubleOrNull()
    val phosphorusValue = phosphorus.toDoubleOrNull()
    val potassiumValue = potassium.toDoubleOrNull()
    val phValue = phLevel.toDoubleOrNull()

    val isPhValid =
        phValue != null && phValue in 0.0..14.0

    val isFormValid =
        nitrogenValue != null &&
                nitrogenValue >= 0 &&
                phosphorusValue != null &&
                phosphorusValue >= 0 &&
                potassiumValue != null &&
                potassiumValue >= 0 &&
                isPhValid

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Crop Recommendation",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor =
                            MaterialTheme.colorScheme.background
                    )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(20.dp)
        ) {

            RecommendationHeader()

            WeatherInputCard(
                temperature = args.temperature,
                humidity = args.humidity,
                rainfall = args.rainfall
            )

            SoilParametersCard(
                nitrogen = nitrogen,
                onNitrogenChange = {
                    nitrogen =
                        filterDecimalInput(it)
                    showResult = false
                },
                phosphorus = phosphorus,
                onPhosphorusChange = {
                    phosphorus =
                        filterDecimalInput(it)
                    showResult = false
                },
                potassium = potassium,
                onPotassiumChange = {
                    potassium =
                        filterDecimalInput(it)
                    showResult = false
                },
                phLevel = phLevel,
                onPhChange = {
                    phLevel =
                        filterDecimalInput(it)
                    showResult = false
                },
                isPhError =
                    phLevel.isNotBlank() &&
                            !isPhValid
            )

            Button(
                onClick = {
                    showResult = true
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape =
                    RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "✨",
                    fontSize = 19.sp
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text =
                        "Analyze & Recommend Crop",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!isFormValid) {
                Text(
                    text =
                        "Enter valid soil parameters to continue.",
                    modifier =
                        Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )
            }

            if (showResult) {
                CropRecommendationResultCard(
                    args = args,
                    phLevel =
                        phValue ?: 0.0,
                    onTryAgain = {
                        showResult = false

                        nitrogen = ""
                        phosphorus = ""
                        potassium = ""
                        phLevel = ""
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


@Composable
private fun RecommendationHeader() {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text =
                "Find the right crop for your farm",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color =
                MaterialTheme.colorScheme
                    .onBackground
        )

        Text(
            text =
                "WeatherRoots combines your soil parameters with live weather conditions to recommend suitable crops.",
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )
    }
}


@Composable
private fun WeatherInputCard(
    temperature: Float,
    humidity: Int,
    rainfall: Float
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(22.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme
                        .primaryContainer
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text =
                            "Live Weather Inputs",
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimaryContainer
                    )

                    Text(
                        text =
                            "Automatically added",
                        fontSize = 13.sp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimaryContainer
                                .copy(alpha = 0.7f)
                    )
                }

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        MaterialTheme
                            .colorScheme
                            .surface
                            .copy(alpha = 0.5f)
                ) {

                    Text(
                        text = "● LIVE",
                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                        fontSize = 11.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )
                }
            }

            HorizontalDivider(
                color =
                    MaterialTheme
                        .colorScheme
                        .onPrimaryContainer
                        .copy(alpha = 0.15f)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                WeatherValueItem(
                    emoji = "🌡️",
                    label = "Temperature",
                    value =
                        "${temperature.formatOneDecimal()}°C",
                    modifier =
                        Modifier.weight(1f)
                )

                WeatherValueItem(
                    emoji = "💧",
                    label = "Humidity",
                    value = "$humidity%",
                    modifier =
                        Modifier.weight(1f)
                )

                WeatherValueItem(
                    emoji = "🌧️",
                    label = "Rainfall",
                    value =
                        "${rainfall.formatOneDecimal()} mm",
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun WeatherValueItem(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(5.dp)
    ) {

        Text(
            text = emoji,
            fontSize = 23.sp
        )

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight =
                FontWeight.Bold,
            color =
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer
        )

        Text(
            text = label,
            fontSize = 11.sp,
            textAlign =
                TextAlign.Center,
            color =
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer
                    .copy(alpha = 0.7f)
        )
    }
}


@Composable
private fun SoilParametersCard(
    nitrogen: String,
    onNitrogenChange: (String) -> Unit,
    phosphorus: String,
    onPhosphorusChange: (String) -> Unit,
    potassium: String,
    onPotassiumChange: (String) -> Unit,
    phLevel: String,
    onPhChange: (String) -> Unit,
    isPhError: Boolean
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(22.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Column {

                Text(
                    text =
                        "🧪 Enter Soil Parameters",
                    fontSize = 19.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        "Enter the latest soil test values from your field.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            SoilInputField(
                value = nitrogen,
                onValueChange =
                    onNitrogenChange,
                label = "Nitrogen (N)",
                placeholder =
                    "Example: 90",
                supportingText =
                    "Nitrogen content in the soil"
            )

            SoilInputField(
                value = phosphorus,
                onValueChange =
                    onPhosphorusChange,
                label = "Phosphorus (P)",
                placeholder =
                    "Example: 42",
                supportingText =
                    "Phosphorus content in the soil"
            )

            SoilInputField(
                value = potassium,
                onValueChange =
                    onPotassiumChange,
                label = "Potassium (K)",
                placeholder =
                    "Example: 43",
                supportingText =
                    "Potassium content in the soil"
            )

            SoilInputField(
                value = phLevel,
                onValueChange =
                    onPhChange,
                label = "Soil pH",
                placeholder =
                    "Example: 6.5",
                supportingText =
                    if (isPhError) {
                        "pH must be between 0 and 14"
                    } else {
                        "Valid pH range: 0.0 to 14.0"
                    },
                isError = isPhError
            )
        }
    }
}


@Composable
private fun SoilInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    supportingText: String,
    isError: Boolean = false
) {

    OutlinedTextField(
        value = value,
        onValueChange =
            onValueChange,
        modifier =
            Modifier.fillMaxWidth(),
        label = {
            Text(
                text = label
            )
        },
        placeholder = {
            Text(
                text = placeholder
            )
        },
        supportingText = {
            Text(
                text = supportingText
            )
        },
        isError = isError,
        singleLine = true,
        shape =
            RoundedCornerShape(14.dp),
        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.Decimal
            )
    )
}


@Composable
private fun CropRecommendationResultCard(
    args: RecommendationRoute,
    phLevel: Double,
    onTryAgain: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(26.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .secondaryContainer
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),
                color =
                    MaterialTheme
                        .colorScheme
                        .surface
                        .copy(alpha = 0.6f)
            ) {

                Text(
                    text =
                        "UI PREVIEW • MOCK RESULT",
                    modifier =
                        Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                    fontSize = 10.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }

            Text(
                text = "🌾",
                fontSize = 60.sp
            )

            Text(
                text =
                    "Recommended Crop",
                fontSize = 15.sp,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSecondaryContainer
                        .copy(alpha = 0.7f)
            )

            Text(
                text = "RICE",
                fontSize = 34.sp,
                fontWeight =
                    FontWeight.Black,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSecondaryContainer
            )

            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),
                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            ) {

                Text(
                    text =
                        "★ High Suitability",
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    color =
                        MaterialTheme
                            .colorScheme
                            .onPrimary,
                    fontSize = 13.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier =
                    Modifier.padding(
                        vertical = 4.dp
                    )
            )

            Column(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                Text(
                    text =
                        "Why this crop?",
                    fontSize = 17.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                RecommendationReason(
                    text =
                        "Suitable current temperature"
                )

                RecommendationReason(
                    text =
                        "Weather conditions are considered"
                )

                RecommendationReason(
                    text =
                        "Soil nutrient values are included"
                )

                RecommendationReason(
                    text =
                        "Soil pH is within a valid range"
                )
            }

            HorizontalDivider()

            Text(
                text =
                    "Conditions used for analysis",
                modifier =
                    Modifier.fillMaxWidth(),
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                ResultMetric(
                    label = "Temperature",
                    value =
                        "${args.temperature.formatOneDecimal()}°C"
                )

                ResultMetric(
                    label = "Humidity",
                    value =
                        "${args.humidity}%"
                )

                ResultMetric(
                    label = "pH",
                    value =
                        phLevel.formatOneDecimal()
                )
            }

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            OutlinedButton(
                onClick = onTryAgain,
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        16.dp
                    )
            ) {

                Text(
                    text =
                        "Try Another Analysis",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun RecommendationReason(
    text: String
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = "✓",
            fontSize = 17.sp,
            fontWeight =
                FontWeight.Bold,
            color =
                MaterialTheme
                    .colorScheme
                    .primary
        )

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Text(
            text = text,
            fontSize = 14.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSecondaryContainer
        )
    }
}


@Composable
private fun ResultMetric(
    label: String,
    value: String
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}


private fun filterDecimalInput(
    input: String
): String {

    if (input.isEmpty()) {
        return input
    }

    var decimalPointFound = false

    return input.filter { character ->

        when {

            character.isDigit() -> {
                true
            }

            character == '.' &&
                    !decimalPointFound -> {

                decimalPointFound = true
                true
            }

            else -> {
                false
            }
        }
    }
}


private fun Float.formatOneDecimal(): String {
    return String.format(
        Locale.getDefault(),
        "%.1f",
        this
    )
}


private fun Double.formatOneDecimal(): String {
    return String.format(
        Locale.getDefault(),
        "%.1f",
        this
    )
}
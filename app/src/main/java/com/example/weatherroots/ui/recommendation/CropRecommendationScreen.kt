package com.example.weatherroots.ui.recommendation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherroots.data.remote.CropRecommendationResponse
import com.example.weatherroots.ui.RecommendationRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropRecommendationScreen(
    args: RecommendationRoute,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CropRecommendationViewModel = viewModel()
) {

    // ---------------------------------------------------------
    // Farmer inputs
    // ---------------------------------------------------------

    var soilType by remember {
        mutableStateOf("")
    }

    var waterAvailability by remember {
        mutableStateOf("")
    }

    var previousCrop by remember {
        mutableStateOf("")
    }

    var season by remember {
        mutableStateOf("")
    }


    // ---------------------------------------------------------
    // Backend/ViewModel state
    // ---------------------------------------------------------

    val isLoading by viewModel.isLoading.collectAsState()

    val recommendation by
    viewModel.recommendation.collectAsState()

    val errorMessage by
    viewModel.errorMessage.collectAsState()


    // ---------------------------------------------------------
    // Form validation
    // ---------------------------------------------------------

    val isFormValid =
        soilType.isNotEmpty() &&
                waterAvailability.isNotEmpty() &&
                previousCrop.isNotEmpty() &&
                season.isNotEmpty()


    // ---------------------------------------------------------
    // Convert farmer-friendly soil names to backend values
    // ---------------------------------------------------------

    fun convertSoilType(
        value: String
    ): String {

        return when (value) {

            "Red Soil" -> "Red"

            "Black Soil" -> "Black"

            "Sandy Soil" -> "Sandy"

            "Loamy Soil" -> "Loamy"

            "Clay Soil" -> "Clay"

            "I don't know" -> "Unknown"

            else -> value
        }
    }


    // ---------------------------------------------------------
    // Convert farmer-friendly seasons to backend seasons
    // ---------------------------------------------------------

    fun convertSeason(
        value: String
    ): String {

        return when (value) {

            "Monsoon" -> "Kharif"

            "Winter" -> "Rabi"

            "Summer" -> "Summer"

            else -> value
        }
    }


    // ---------------------------------------------------------
    // Main screen
    // ---------------------------------------------------------

    Scaffold(

        topBar = {

            TopAppBar(

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
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->


        Column(

            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(18.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            // -------------------------------------------------
            // Weather information
            // -------------------------------------------------

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .primaryContainer
                    )
            ) {


                Column(

                    modifier =
                        Modifier.padding(20.dp)

                ) {


                    Text(

                        text =
                            "🌦 Auto-filled Weather Data",

                        fontSize = 20.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {


                        WeatherInfoItem(

                            icon = "🌡️",

                            label =
                                "Temperature",

                            value =
                                "${args.temperature}°C"
                        )


                        WeatherInfoItem(

                            icon = "💧",

                            label =
                                "Humidity",

                            value =
                                "${args.humidity}%"
                        )


                        WeatherInfoItem(

                            icon = "🌧️",

                            label =
                                "Current Rainfall",

                            value =
                                "${args.rainfall} mm"
                        )
                    }
                }
            }


            // -------------------------------------------------
            // Heading
            // -------------------------------------------------

            Text(

                text =
                    "🌱 Tell us about your farm",

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.Bold,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Text(

                text =
                    "Answer simple questions to get the best crop suggestion",

                fontSize = 15.sp,

                modifier =
                    Modifier.fillMaxWidth()
            )


            // -------------------------------------------------
            // Farmer details
            // -------------------------------------------------

            FarmerDetailsCard(

                soilType =
                    soilType,

                onSoilSelected = {

                    soilType = it

                    viewModel.clearResult()
                },


                waterAvailability =
                    waterAvailability,

                onWaterSelected = {

                    waterAvailability = it

                    viewModel.clearResult()
                },


                previousCrop =
                    previousCrop,

                onCropSelected = {

                    previousCrop = it

                    viewModel.clearResult()
                },


                season =
                    season,

                onSeasonSelected = {

                    season = it

                    viewModel.clearResult()
                }
            )


            // -------------------------------------------------
            // Error message
            // -------------------------------------------------

            errorMessage?.let { message ->

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .errorContainer
                        )
                ) {

                    Text(

                        text = message,

                        modifier =
                            Modifier.padding(16.dp),

                        color =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer
                    )
                }
            }


            // -------------------------------------------------
            // Recommendation button
            // -------------------------------------------------

            Button(

                onClick = {

                    val backendSoil =
                        convertSoilType(
                            soilType
                        )


                    val backendSeason =
                        convertSeason(
                            season
                        )


                    viewModel.getRecommendation(

                        temperature =
                            args.temperature.toDouble(),

                        humidity =
                            args.humidity.toDouble(),

                        rainfall =
                            args.rainfall.toDouble(),

                        soilType =
                            backendSoil,

                        waterAvailability =
                            waterAvailability,

                        previousCrop =
                            previousCrop,

                        season =
                            backendSeason
                    )
                },

                enabled =
                    isFormValid &&
                            !isLoading,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                shape =
                    RoundedCornerShape(14.dp)

            ) {


                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(24.dp),

                        strokeWidth =
                            3.dp,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary
                    )


                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )


                    Text(
                        text =
                            "Getting Recommendation..."
                    )

                } else {

                    Text(

                        text =
                            "🌱 Get Crop Recommendation",

                        fontSize = 17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }


    // ---------------------------------------------------------
    // Recommendation result
    // ---------------------------------------------------------

    recommendation?.let { result ->

        RecommendationResultDialog(

            result = result,

            onDismiss = {

                viewModel.clearResult()
            }
        )
    }
}


// =============================================================
// Weather Information
// =============================================================

@Composable
private fun WeatherInfoItem(

    icon: String,

    label: String,

    value: String

) {

    Column(

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {


        Text(

            text = icon,

            fontSize = 22.sp
        )


        Spacer(

            modifier =
                Modifier.height(4.dp)
        )


        Text(

            text = value,

            fontWeight =
                FontWeight.Bold,

            fontSize = 14.sp
        )


        Text(

            text = label,

            fontSize = 11.sp
        )
    }
}


// =============================================================
// Farmer Details
// =============================================================

@Composable
private fun FarmerDetailsCard(

    soilType: String,

    onSoilSelected:
        (String) -> Unit,

    waterAvailability:
    String,

    onWaterSelected:
        (String) -> Unit,

    previousCrop:
    String,

    onCropSelected:
        (String) -> Unit,

    season:
    String,

    onSeasonSelected:
        (String) -> Unit

) {


    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

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


            Text(

                text =
                    "🌱 Farm Details",

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold
            )


            // Soil type

            FarmerDropdown(

                title =
                    "Soil Type",

                selected =
                    soilType,

                options =
                    listOf(

                        "Red Soil",

                        "Black Soil",

                        "Sandy Soil",

                        "Loamy Soil",

                        "Clay Soil",

                        "I don't know"
                    ),

                onSelected =
                    onSoilSelected
            )


            // Water availability

            FarmerDropdown(

                title =
                    "Water Availability",

                selected =
                    waterAvailability,

                options =
                    listOf(

                        "High",

                        "Medium",

                        "Low"
                    ),

                onSelected =
                    onWaterSelected
            )


            // Previous crop

            FarmerDropdown(

                title =
                    "Previous Crop",

                selected =
                    previousCrop,

                options =
                    listOf(

                        "Rice",

                        "Maize",

                        "Cotton",

                        "Groundnut",

                        "First time farming"
                    ),

                onSelected =
                    onCropSelected
            )


            // Season

            FarmerDropdown(

                title =
                    "Season",

                selected =
                    season,

                options =
                    listOf(

                        "Summer",

                        "Monsoon",

                        "Winter"
                    ),

                onSelected =
                    onSeasonSelected
            )
        }
    }
}


// =============================================================
// Farmer Dropdown
// =============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FarmerDropdown(

    title: String,

    selected: String,

    options: List<String>,

    onSelected:
        (String) -> Unit

) {


    var expanded by remember {

        mutableStateOf(false)
    }


    ExposedDropdownMenuBox(

        expanded =
            expanded,

        onExpandedChange = {

            expanded =
                !expanded
        }

    ) {


        OutlinedTextField(

            value =
                selected,

            onValueChange = {},

            readOnly =
                true,

            label = {

                Text(
                    title
                )
            },

            placeholder = {

                Text(
                    "Select"
                )
            },

            trailingIcon = {

                ExposedDropdownMenuDefaults
                    .TrailingIcon(

                        expanded =
                            expanded
                    )
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor()
        )


        ExposedDropdownMenu(

            expanded =
                expanded,

            onDismissRequest = {

                expanded =
                    false
            }

        ) {


            options.forEach { item ->


                DropdownMenuItem(

                    text = {

                        Text(
                            item
                        )
                    },

                    onClick = {

                        onSelected(
                            item
                        )

                        expanded =
                            false
                    }
                )
            }
        }
    }
}


// =============================================================
// Recommendation Result Dialog
// =============================================================

@Composable
private fun RecommendationResultDialog(

    result:
    CropRecommendationResponse,

    onDismiss:
        () -> Unit

) {


    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "🌾 AI Crop Recommendation"
            )
        },

        text = {


            Column(

                modifier =
                    Modifier.verticalScroll(
                        rememberScrollState()
                    )

            ) {


                // -------------------------------------------------
                // Recommended crop
                // -------------------------------------------------

                Text(

                    text =
                        "Recommended Crop",

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(8.dp)
                )


                Text(

                    text =
                        result.recommended_crop,

                    fontSize =
                        28.sp,

                    fontWeight =
                        FontWeight.Black,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )


                Spacer(

                    modifier =
                        Modifier.height(8.dp)
                )


                // -------------------------------------------------
                // Suitability score
                // -------------------------------------------------

                Text(

                    text =
                        "Suitability Score: " +
                                String.format(
                                    "%.2f%%",
                                    result.suitability_score
                                ),

                    fontWeight =
                        FontWeight.SemiBold
                )


                // -------------------------------------------------
                // Alternative crops
                // -------------------------------------------------

                if (
                    result.alternatives
                        .isNotEmpty()
                ) {


                    Spacer(

                        modifier =
                            Modifier.height(20.dp)
                    )


                    Text(

                        text =
                            "Alternative Crops",

                        fontWeight =
                            FontWeight.Bold
                    )


                    Spacer(

                        modifier =
                            Modifier.height(8.dp)
                    )


                    result.alternatives
                        .forEach { alternative ->


                            Text(

                                text =
                                    "• ${alternative.crop} — " +
                                            String.format(
                                                "%.2f%%",
                                                alternative
                                                    .suitability_score
                                            )
                            )
                        }
                }


                // -------------------------------------------------
                // Explanation
                // -------------------------------------------------

                Spacer(

                    modifier =
                        Modifier.height(20.dp)
                )


                Text(

                    text =
                        "Why this crop?",

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(6.dp)
                )


                Text(

                    text =
                        result.explanation
                )


                // -------------------------------------------------
                // Rainfall information
                // -------------------------------------------------

                Spacer(

                    modifier =
                        Modifier.height(20.dp)
                )


                Text(

                    text =
                        "Rainfall Information",

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(

                    modifier =
                        Modifier.height(6.dp)
                )


                Text(

                    text =
                        "Current rainfall: " +
                                "${result.current_rainfall} mm"
                )


                Text(

                    text =
                        "Historical annual rainfall: " +
                                "${result.climate_rainfall} mm"
                )


                Spacer(

                    modifier =
                        Modifier.height(4.dp)
                )


                Text(

                    text =
                        result.rainfall_source,

                    fontSize =
                        11.sp,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }
        },

        confirmButton = {

            TextButton(

                onClick =
                    onDismiss

            ) {

                Text(
                    "OK"
                )
            }
        }
    )
}
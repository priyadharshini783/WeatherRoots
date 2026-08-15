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
import com.example.weatherroots.ui.RecommendationRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropRecommendationScreen(
    args: RecommendationRoute,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Farmer-friendly inputs
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


    var showResult by remember {
        mutableStateOf(false)
    }


    val isFormValid =
        soilType.isNotEmpty() &&
                waterAvailability.isNotEmpty() &&
                previousCrop.isNotEmpty() &&
                season.isNotEmpty()



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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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


            verticalArrangement = Arrangement.spacedBy(18.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {



            // Weather information automatically received from dashboard

            Card(

                modifier = Modifier
                    .fillMaxWidth(),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(

                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer

                )

            ) {


                Column(

                    modifier = Modifier.padding(20.dp)

                ) {



                    Text(

                        text = "🌦 Auto-filled Weather Data",

                        fontSize = 20.sp,

                        fontWeight = FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.primary

                    )


                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )


                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {


                        WeatherInfoItem(

                            icon = "🌡️",

                            label = "Temperature",

                            value = "${args.temperature}°C"

                        )


                        WeatherInfoItem(

                            icon = "💧",

                            label = "Humidity",

                            value = "${args.humidity}%"

                        )


                        WeatherInfoItem(

                            icon = "🌧️",

                            label = "Rainfall",

                            value = "${args.rainfall} mm"

                        )

                    }

                }

            }





            Text(

                text = "🌱 Tell us about your farm",

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold,

                modifier = Modifier.fillMaxWidth()

            )



            Text(

                text = "Answer simple questions to get the best crop suggestion",

                fontSize = 15.sp,

                modifier = Modifier.fillMaxWidth()

            )





            FarmerDetailsCard(

                soilType = soilType,

                onSoilSelected = {

                    soilType = it
                    showResult = false

                },


                waterAvailability = waterAvailability,

                onWaterSelected = {

                    waterAvailability = it
                    showResult = false

                },


                previousCrop = previousCrop,

                onCropSelected = {

                    previousCrop = it
                    showResult = false

                },


                season = season,

                onSeasonSelected = {

                    season = it
                    showResult = false

                }

            )





            Button(

                onClick = {

                    showResult = true

                },


                enabled = isFormValid,


                modifier = Modifier

                    .fillMaxWidth()

                    .height(56.dp),


                shape = RoundedCornerShape(14.dp)

            ) {


                Text(

                    text = "🌱 Get Crop Recommendation",

                    fontSize = 17.sp,

                    fontWeight = FontWeight.Bold

                )


            }


        }

    }



    if(showResult){

        RecommendationResultDialog(

            onDismiss = {

                showResult = false

            }

        )

    }


}
@Composable
private fun WeatherInfoItem(
    icon: String,
    label: String,
    value: String
) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        Text(

            text = icon,

            fontSize = 22.sp

        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Text(

            text = value,

            fontWeight = FontWeight.Bold,

            fontSize = 14.sp

        )


        Text(

            text = label,

            fontSize = 11.sp

        )

    }

}





@Composable
private fun FarmerDetailsCard(

    soilType: String,

    onSoilSelected: (String) -> Unit,


    waterAvailability: String,

    onWaterSelected: (String) -> Unit,


    previousCrop: String,

    onCropSelected: (String) -> Unit,


    season: String,

    onSeasonSelected: (String) -> Unit

) {


    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(

            containerColor =
                MaterialTheme.colorScheme.surfaceVariant

        )

    ) {


        Column(

            modifier = Modifier.padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {



            Text(

                text = "🌱 Farm Details",

                fontSize = 20.sp,

                fontWeight = FontWeight.Bold

            )



            FarmerDropdown(

                title = "Soil Type",

                selected = soilType,

                options = listOf(

                    "Red Soil",

                    "Black Soil",

                    "Sandy Soil",

                    "Loamy Soil",

                    "Clay Soil",

                    "I don't know"

                ),

                onSelected = onSoilSelected

            )





            FarmerDropdown(

                title = "Water Availability",

                selected = waterAvailability,

                options = listOf(

                    "High",

                    "Medium",

                    "Low"

                ),

                onSelected = onWaterSelected

            )





            FarmerDropdown(

                title = "Previous Crop",

                selected = previousCrop,

                options = listOf(

                    "Rice",

                    "Maize",

                    "Cotton",

                    "Groundnut",

                    "First time farming"

                ),

                onSelected = onCropSelected

            )





            FarmerDropdown(

                title = "Season",

                selected = season,

                options = listOf(

                    "Summer",

                    "Monsoon",

                    "Winter"

                ),

                onSelected = onSeasonSelected

            )

        }

    }

}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FarmerDropdown(

    title: String,

    selected: String,

    options: List<String>,

    onSelected: (String) -> Unit

) {


    var expanded by remember {

        mutableStateOf(false)

    }



    ExposedDropdownMenuBox(

        expanded = expanded,

        onExpandedChange = {

            expanded = !expanded

        }

    ) {


        OutlinedTextField(

            value = selected,

            onValueChange = {},

            readOnly = true,


            label = {

                Text(title)

            },


            placeholder = {

                Text(
                    "Select"
                )

            },


            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(

                    expanded = expanded

                )

            },


            modifier = Modifier

                .fillMaxWidth()

                .menuAnchor()

        )




        ExposedDropdownMenu(

            expanded = expanded,


            onDismissRequest = {

                expanded = false

            }

        ) {


            options.forEach { item ->


                DropdownMenuItem(

                    text = {

                        Text(item)

                    },


                    onClick = {


                        onSelected(item)


                        expanded = false


                    }

                )


            }

        }


    }

}







@Composable
private fun RecommendationResultDialog(

    onDismiss: () -> Unit

) {


    AlertDialog(


        onDismissRequest = onDismiss,


        title = {


            Text(

                "🌾 AI Crop Recommendation"

            )

        },


        text = {


            Column {


                Text(

                    text = "Recommended Crop",

                    fontWeight = FontWeight.Bold

                )


                Spacer(

                    modifier = Modifier.height(8.dp)

                )


                Text(

                    text = "🌾 Rice",

                    fontSize = 28.sp,

                    fontWeight = FontWeight.Black

                )


                Spacer(

                    modifier = Modifier.height(12.dp)

                )


                Text(

                    "Reason:\n" +

                            "✓ Suitable weather condition\n" +

                            "✓ Water availability matches\n" +

                            "✓ Season is favourable"

                )


            }


        },


        confirmButton = {


            TextButton(

                onClick = onDismiss

            ) {


                Text("OK")


            }

        }


    )

}
package com.example.weatherroots.ui.voiceassistant


import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.content.ContextCompat

import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun VoiceAssistantScreen(

    onNavigateBack: () -> Unit,

    viewModel: VoiceAssistantViewModel =
        viewModel()

) {

    val context =
        LocalContext.current


    // =========================================================
    // BACKEND STATE
    // =========================================================

    val backendResponse by
    viewModel.response.collectAsState()


    val isLoading by
    viewModel.isLoading.collectAsState()


    val errorMessage by
    viewModel.errorMessage.collectAsState()


    // =========================================================
    // SCREEN STATE
    // =========================================================

    var selectedLanguage by remember {
        mutableStateOf("English")
    }


    var languageExpanded by remember {
        mutableStateOf(false)
    }


    var question by remember {
        mutableStateOf("")
    }


    var isListening by remember {
        mutableStateOf(false)
    }


    // =========================================================
    // MANAGERS
    // =========================================================

    val speechManager =
        remember {

            SpeechRecognizerManager(
                context
            )
        }


    val ttsManager =
        remember {

            TextToSpeechManager(
                context
            )
        }


    // =========================================================
    // MICROPHONE PERMISSION
    // =========================================================

    var hasPermission by remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }


    val permissionLauncher =
        rememberLauncherForActivityResult(

            ActivityResultContracts.RequestPermission()

        ) { granted ->

            hasPermission =
                granted
        }


    // =========================================================
    // CLEANUP
    // =========================================================

    DisposableEffect(Unit) {

        onDispose {

            speechManager.stopListening()

            ttsManager.shutdown()
        }
    }


    // =========================================================
    // MICROPHONE ANIMATION
    // =========================================================

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "microphone"
        )


    val microphoneScale by
    infiniteTransition.animateFloat(

        initialValue = 1f,

        targetValue =
            if (isListening)
                1.12f
            else
                1.04f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis = 800,
                        easing =
                            FastOutSlowInEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label =
            "microphoneScale"
    )


    // =========================================================
    // COLORS
    // =========================================================

    val darkGreen =
        Color(0xFF174D2A)


    val primaryGreen =
        Color(0xFF2E7D4F)


    val brightGreen =
        Color(0xFF43A047)


    val softGreen =
        Color(0xFFF1F8F2)


    val paleGreen =
        Color(0xFFE5F3E8)


    val textDark =
        Color(0xFF17231B)


    val textSecondary =
        Color(0xFF66736A)


    val languages =
        listOf(
            "English",
            "Tamil",
            "Hindi",
            "Telugu"
        )


    // =========================================================
    // MAIN SCREEN
    // =========================================================

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF8FCF8)
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 18.dp
                )

    ) {


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        // =====================================================
        // TOP BAR
        // =====================================================

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically

        ) {


            Surface(

                modifier =
                    Modifier
                        .size(44.dp)
                        .clickable {
                            onNavigateBack()
                        },

                shape =
                    CircleShape,

                color =
                    Color.White,

                shadowElevation =
                    2.dp

            ) {

                Box(

                    contentAlignment =
                        Alignment.Center

                ) {

                    Text(
                        text = "←",
                        fontSize = 25.sp,
                        color = textDark
                    )
                }
            }


            Spacer(
                Modifier.width(12.dp)
            )


            Column(

                modifier =
                    Modifier.weight(1f)

            ) {

                Text(

                    text =
                        "WeatherRoots AI",

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        textDark
                )


                Text(

                    text =
                        "Your farming companion",

                    fontSize =
                        13.sp,

                    color =
                        textSecondary
                )
            }


            Surface(

                shape =
                    RoundedCornerShape(
                        20.dp
                    ),

                color =
                    paleGreen

            ) {

                Row(

                    modifier =
                        Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 7.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    Text(
                        "●",
                        color = brightGreen,
                        fontSize = 12.sp
                    )


                    Spacer(
                        Modifier.width(5.dp)
                    )


                    Text(

                        "AI Online",

                        color =
                            darkGreen,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }


        Spacer(
            Modifier.height(22.dp)
        )


        // =====================================================
        // HERO CARD
        // =====================================================

        Card(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        8.dp,
                        RoundedCornerShape(
                            28.dp
                        )
                    ),

            shape =
                RoundedCornerShape(
                    28.dp
                ),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.Transparent
                )

        ) {


            Box(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(

                            brush =
                                Brush.linearGradient(

                                    listOf(
                                        Color(0xFF174D2A),
                                        Color(0xFF2E7D4F)
                                    )
                                )
                        )
                        .padding(
                            24.dp
                        )

            ) {


                Column {

                    Surface(

                        shape =
                            CircleShape,

                        color =
                            Color.White.copy(
                                alpha = 0.15f
                            )

                    ) {

                        Text(

                            text =
                                "🌱",

                            fontSize =
                                38.sp,

                            modifier =
                                Modifier.padding(
                                    13.dp
                                )
                        )
                    }


                    Spacer(
                        Modifier.height(18.dp)
                    )


                    Text(

                        text =
                            "Ask. Learn. Grow.",

                        color =
                            Color.White,

                        fontSize =
                            27.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    Spacer(
                        Modifier.height(7.dp)
                    )


                    Text(

                        text =
                            "Get practical farming guidance in your preferred language.",

                        color =
                            Color.White.copy(
                                alpha = 0.85f
                            ),

                        fontSize =
                            15.sp,

                        lineHeight =
                            21.sp
                    )
                }
            }
        }


        Spacer(
            Modifier.height(24.dp)
        )


        // =====================================================
        // LANGUAGE
        // =====================================================

        Text(

            text =
                "Choose your language",

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                textDark
        )


        Spacer(
            Modifier.height(11.dp)
        )


        Box {


            Surface(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            languageExpanded = true
                        },

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                color =
                    Color.White,

                shadowElevation =
                    2.dp

            ) {


                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 17.dp,
                                vertical = 15.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {


                    Text(
                        "🌐",
                        fontSize = 22.sp
                    )


                    Spacer(
                        Modifier.width(12.dp)
                    )


                    Column(

                        modifier =
                            Modifier.weight(1f)

                    ) {

                        Text(

                            "Farmer language",

                            fontSize =
                                12.sp,

                            color =
                                textSecondary
                        )


                        Text(

                            selectedLanguage,

                            fontSize =
                                17.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            color =
                                textDark
                        )
                    }


                    Text(
                        "⌄",
                        fontSize = 24.sp,
                        color = textSecondary
                    )
                }
            }


            DropdownMenu(

                expanded =
                    languageExpanded,

                onDismissRequest = {

                    languageExpanded =
                        false
                }

            ) {


                languages.forEach {
                        language ->


                    DropdownMenuItem(

                        text = {

                            Text(
                                language
                            )
                        },

                        onClick = {

                            selectedLanguage =
                                language

                            languageExpanded =
                                false
                        }
                    )
                }
            }
        }


        Spacer(
            Modifier.height(22.dp)
        )


        // =====================================================
        // QUESTION
        // =====================================================

        Text(

            text =
                "What's on your mind?",

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                textDark
        )


        Spacer(
            Modifier.height(10.dp)
        )


        OutlinedTextField(

            value =
                question,

            onValueChange = {

                question = it
            },

            placeholder = {

                Text(
                    "e.g. Which crop is best for black soil?"
                )
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 110.dp
                    ),

            shape =
                RoundedCornerShape(
                    20.dp
                ),

            colors =
                OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =
                        primaryGreen,

                    unfocusedBorderColor =
                        Color(0xFFD7E2D9),

                    focusedContainerColor =
                        Color.White,

                    unfocusedContainerColor =
                        Color.White
                )
        )


        Spacer(
            Modifier.height(22.dp)
        )


        // =====================================================
        // MICROPHONE AREA
        // =====================================================

        Column(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            Surface(

                modifier =
                    Modifier
                        .size(92.dp)
                        .graphicsLayer {

                            scaleX =
                                microphoneScale

                            scaleY =
                                microphoneScale
                        }
                        .clickable {


                            if (
                                hasPermission
                            ) {

                                isListening =
                                    true


                                speechManager
                                    .startListening(

                                        selectedLanguage

                                    ) { spokenText ->


                                        question =
                                            spokenText


                                        isListening =
                                            false
                                    }

                            } else {

                                permissionLauncher
                                    .launch(
                                        Manifest.permission.RECORD_AUDIO
                                    )
                            }
                        },

                shape =
                    CircleShape,

                color =
                    primaryGreen,

                shadowElevation =
                    10.dp

            ) {


                Box(

                    contentAlignment =
                        Alignment.Center

                ) {

                    Text(

                        text =
                            "🎙️",

                        fontSize =
                            39.sp
                    )
                }
            }


            Spacer(
                Modifier.height(13.dp)
            )


            Text(

                text =
                    if (isListening)
                        "Listening..."
                    else
                        "Tap to speak",

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    if (isListening)
                        primaryGreen
                    else
                        textDark
            )


            Text(

                text =
                    "Speak naturally in $selectedLanguage",

                fontSize =
                    13.sp,

                color =
                    textSecondary
            )
        }


        Spacer(
            Modifier.height(25.dp)
        )


        // =====================================================
        // ASK AI BUTTON
        // =====================================================

        Button(

            onClick = {

                viewModel.askQuestion(
                    question
                )
            },

            enabled =
                question.isNotBlank()
                        &&
                        !isLoading,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(58.dp),

            shape =
                RoundedCornerShape(
                    18.dp
                ),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        primaryGreen,

                    disabledContainerColor =
                        Color(0xFFAFC8B5)
                )

        ) {


            if (isLoading) {


                CircularProgressIndicator(

                    modifier =
                        Modifier.size(
                            22.dp
                        ),

                    strokeWidth =
                        2.dp,

                    color =
                        Color.White
                )


                Spacer(
                    Modifier.width(10.dp)
                )


                Text(
                    "WeatherRoots is thinking..."
                )

            } else {


                Text(
                    "✨"
                )


                Spacer(
                    Modifier.width(8.dp)
                )


                Text(

                    text =
                        "Ask WeatherRoots AI",

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        // =====================================================
        // ERROR
        // =====================================================

        errorMessage?.let {
                error ->


            Spacer(
                Modifier.height(18.dp)
            )


            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        16.dp
                    ),

                color =
                    Color(0xFFFFE9E7)

            ) {


                Text(

                    text =
                        "⚠️ $error",

                    modifier =
                        Modifier.padding(
                            16.dp
                        ),

                    color =
                        Color(0xFF9B2C2C),

                    fontSize =
                        14.sp
                )
            }
        }


        // =====================================================
        // AI RESPONSE
        // =====================================================

        backendResponse?.let {
                result ->


            Spacer(
                Modifier.height(22.dp)
            )


            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color =
                                Color(0xFFDDEBDD),
                            shape =
                                RoundedCornerShape(
                                    24.dp
                                )
                        ),

                shape =
                    RoundedCornerShape(
                        24.dp
                    ),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                            3.dp
                    )

            ) {


                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )

                ) {


                    Row(

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {


                        Surface(

                            shape =
                                CircleShape,

                            color =
                                paleGreen

                        ) {


                            Text(

                                text =
                                    "🌿",

                                modifier =
                                    Modifier.padding(
                                        10.dp
                                    ),

                                fontSize =
                                    23.sp
                            )
                        }


                        Spacer(
                            Modifier.width(11.dp)
                        )


                        Column {

                            Text(

                                text =
                                    "WeatherRoots AI",

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize =
                                    18.sp,

                                color =
                                    textDark
                            )


                            Text(

                                text =
                                    "Farming advice",

                                fontSize =
                                    12.sp,

                                color =
                                    textSecondary
                            )
                        }
                    }


                    Spacer(
                        Modifier.height(17.dp)
                    )


                    Text(

                        text =
                            result.response,

                        fontSize =
                            16.sp,

                        lineHeight =
                            24.sp,

                        color =
                            Color(0xFF303A33)
                    )


                    Spacer(
                        Modifier.height(18.dp)
                    )


                    HorizontalDivider(

                        color =
                            Color(0xFFE9EFEA)
                    )


                    Spacer(
                        Modifier.height(14.dp)
                    )


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {


                        Text(

                            text =
                                "🌐 ${result.detected_language}",

                            modifier =
                                Modifier.weight(1f),

                            fontSize =
                                13.sp,

                            color =
                                textSecondary
                        )


                        FilledTonalButton(

                            onClick = {

                                ttsManager.speak(

                                    result.response,

                                    result.detected_language
                                )
                            },

                            colors =
                                ButtonDefaults
                                    .filledTonalButtonColors(

                                        containerColor =
                                            softGreen,

                                        contentColor =
                                            primaryGreen
                                    )

                        ) {

                            Text(
                                "🔊  Listen"
                            )
                        }
                    }
                }
            }
        }


        // =====================================================
        // EMPTY RESPONSE PLACEHOLDER
        // =====================================================

        if (
            backendResponse == null
            &&
            !isLoading
        ) {


            Spacer(
                Modifier.height(22.dp)
            )


            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        20.dp
                    ),

                color =
                    softGreen

            ) {


                Column(

                    modifier =
                        Modifier.padding(
                            22.dp
                        ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ) {


                    Text(
                        "🌾",
                        fontSize = 34.sp
                    )


                    Spacer(
                        Modifier.height(8.dp)
                    )


                    Text(

                        text =
                            "Your farming advice will appear here",

                        textAlign =
                            TextAlign.Center,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            textDark
                    )


                    Spacer(
                        Modifier.height(4.dp)
                    )


                    Text(

                        text =
                            "Ask about crops, soil, irrigation, weather or farming practices.",

                        textAlign =
                            TextAlign.Center,

                        fontSize =
                            13.sp,

                        color =
                            textSecondary
                    )
                }
            }
        }


        Spacer(
            Modifier.height(35.dp)
        )
    }
}
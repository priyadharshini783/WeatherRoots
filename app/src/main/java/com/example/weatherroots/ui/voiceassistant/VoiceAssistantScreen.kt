package com.example.weatherroots.ui.voiceassistant

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
    viewModel: VoiceAssistantViewModel = viewModel()
) {

    val context = LocalContext.current


    // =========================================================
    // ROOM + VIEWMODEL STATE
    // =========================================================

    val messages by
    viewModel.messages.collectAsState()

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
            SpeechRecognizerManager(context)
        }

    val ttsManager =
        remember {
            TextToSpeechManager(context)
        }


    // =========================================================
    // MICROPHONE PERMISSION
    // =========================================================

    var hasPermission by remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }


    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasPermission = granted
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
            if (isListening) {
                1.12f
            } else {
                1.04f
            },

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label = "microphoneScale"
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
                modifier =
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
                        text = "●",
                        color = brightGreen,
                        fontSize = 12.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )


                    Text(

                        text =
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
            modifier =
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

                            text = "🌱",

                            fontSize =
                                38.sp,

                            modifier =
                                Modifier.padding(
                                    13.dp
                                )
                        )
                    }


                    Spacer(
                        modifier =
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
                        modifier =
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
            modifier =
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
            modifier =
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
                        text = "🌐",
                        fontSize = 22.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )


                    Column(

                        modifier =
                            Modifier.weight(1f)

                    ) {


                        Text(

                            text =
                                "Farmer language",

                            fontSize =
                                12.sp,

                            color =
                                textSecondary
                        )


                        Text(

                            text =
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
                        text = "⌄",
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


                languages.forEach { language ->


                    DropdownMenuItem(

                        text = {

                            Text(
                                text =
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
            modifier =
                Modifier.height(22.dp)
        )


        // =====================================================
        // QUESTION INPUT
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
            modifier =
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
                    text =
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
            modifier =
                Modifier.height(22.dp)
        )


        // =====================================================
        // MICROPHONE
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


                            if (hasPermission) {


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
                modifier =
                    Modifier.height(13.dp)
            )


            Text(

                text =
                    if (isListening) {
                        "Listening..."
                    } else {
                        "Tap to speak"
                    },

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    if (isListening) {
                        primaryGreen
                    } else {
                        textDark
                    }
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
            modifier =
                Modifier.height(25.dp)
        )


        // =====================================================
        // ASK AI BUTTON
        // =====================================================

        Button(

            onClick = {


                val currentQuestion =
                    question.trim()


                if (
                    currentQuestion
                        .isNotEmpty()
                ) {


                    viewModel
                        .askQuestion(

                            question =
                                currentQuestion,

                            selectedLanguage =
                                selectedLanguage
                        )


                    // Clear textbox after sending
                    question = ""
                }
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
                    modifier =
                        Modifier.width(10.dp)
                )


                Text(
                    text =
                        "WeatherRoots is thinking..."
                )

            } else {


                Text(
                    text =
                        "✨"
                )


                Spacer(
                    modifier =
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

        errorMessage?.let { error ->


            Spacer(
                modifier =
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
        // CONVERSATION HISTORY
        // =====================================================

        if (
            messages.isNotEmpty()
        ) {


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically

            ) {


                Column(

                    modifier =
                        Modifier.weight(1f)

                ) {


                    Text(

                        text =
                            "Conversation",

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            textDark
                    )


                    Text(

                        text =
                            "${messages.size} messages saved on this device",

                        fontSize =
                            12.sp,

                        color =
                            textSecondary
                    )
                }


                TextButton(

                    onClick = {

                        viewModel
                            .clearConversation()
                    }

                ) {


                    Text(

                        text =
                            "Clear",

                        color =
                            Color(0xFFB3261E),

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            messages.forEach {
                    message ->


                VoiceChatBubble(

                    message =
                        message,

                    onSpeak = {
                            selectedMessage ->


                        ttsManager.speak(

                            selectedMessage.text,

                            selectedMessage.language
                        )
                    }
                )


                Spacer(
                    modifier =
                        Modifier.height(11.dp)
                )
            }
        }


        // =====================================================
        // EMPTY CONVERSATION
        // =====================================================

        if (
            messages.isEmpty()
            &&
            !isLoading
        ) {


            Spacer(
                modifier =
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
                        text = "🌾",
                        fontSize = 34.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    Text(

                        text =
                            "Start a farming conversation",

                        textAlign =
                            TextAlign.Center,

                        fontWeight =
                            FontWeight.SemiBold,

                        color =
                            textDark
                    )


                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )


                    Text(

                        text =
                            "Your questions and WeatherRoots answers will be saved on this device.",

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
            modifier =
                Modifier.height(35.dp)
        )
    }
}


// =============================================================
// CHAT BUBBLE
// =============================================================

@Composable
private fun VoiceChatBubble(

    message:
    VoiceChatMessage,

    onSpeak:
        (VoiceChatMessage) -> Unit

) {


    val primaryGreen =
        Color(0xFF2E7D4F)


    val userBubble =
        primaryGreen


    val aiBubble =
        Color.White


    val textDark =
        Color(0xFF263129)


    Row(

        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            if (
                message.isUser
            ) {

                Arrangement.End

            } else {

                Arrangement.Start
            }

    ) {


        Card(

            modifier =
                Modifier.widthIn(
                    max = 315.dp
                ),

            shape =
                RoundedCornerShape(

                    topStart =
                        20.dp,

                    topEnd =
                        20.dp,

                    bottomStart =
                        if (
                            message.isUser
                        ) {
                            20.dp
                        } else {
                            5.dp
                        },

                    bottomEnd =
                        if (
                            message.isUser
                        ) {
                            5.dp
                        } else {
                            20.dp
                        }
                ),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        if (
                            message.isUser
                        ) {
                            userBubble
                        } else {
                            aiBubble
                        }
                ),

            elevation =
                CardDefaults.cardElevation(

                    defaultElevation =
                        if (
                            message.isUser
                        ) {
                            1.dp
                        } else {
                            3.dp
                        }
                )

        ) {


            Column(

                modifier =
                    Modifier.padding(
                        15.dp
                    )

            ) {


                // =================================================
                // AI HEADER
                // =================================================

                if (
                    !message.isUser
                ) {


                    Row(

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {


                        Surface(

                            shape =
                                CircleShape,

                            color =
                                Color(0xFFE5F3E8)

                        ) {


                            Text(

                                text =
                                    "🌿",

                                modifier =
                                    Modifier.padding(
                                        7.dp
                                    ),

                                fontSize =
                                    17.sp
                            )
                        }


                        Spacer(
                            modifier =
                                Modifier.width(7.dp)
                        )


                        Column {


                            Text(

                                text =
                                    "WeatherRoots AI",

                                fontSize =
                                    12.sp,

                                fontWeight =
                                    FontWeight.Bold,

                                color =
                                    primaryGreen
                            )


                            Text(

                                text =
                                    "Farming advice",

                                fontSize =
                                    10.sp,

                                color =
                                    Color.Gray
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }


                // =================================================
                // MESSAGE
                // =================================================

                Text(

                    text =
                        message.text,

                    fontSize =
                        15.sp,

                    lineHeight =
                        22.sp,

                    color =
                        if (
                            message.isUser
                        ) {
                            Color.White
                        } else {
                            textDark
                        }
                )


                // =================================================
                // LANGUAGE + LISTEN
                // =================================================

                if (
                    !message.isUser
                ) {


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    HorizontalDivider(
                        color =
                            Color(0xFFE8EFE9)
                    )


                    Spacer(
                        modifier =
                            Modifier.height(7.dp)
                    )


                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {


                        Text(

                            text =
                                languageLabel(
                                    message.language
                                ),

                            fontSize =
                                11.sp,

                            color =
                                Color.Gray,

                            modifier =
                                Modifier.weight(1f)
                        )


                        FilledTonalButton(

                            onClick = {

                                onSpeak(
                                    message
                                )
                            },

                            colors =
                                ButtonDefaults
                                    .filledTonalButtonColors(

                                        containerColor =
                                            Color(0xFFF1F8F2),

                                        contentColor =
                                            primaryGreen
                                    ),

                            contentPadding =
                                PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 5.dp
                                )

                        ) {


                            Text(
                                text =
                                    "🔊 Listen",
                                fontSize =
                                    12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// =============================================================
// LANGUAGE LABEL
// =============================================================

private fun languageLabel(
    code: String
): String {

    return when (
        code
            .trim()
            .lowercase()
    ) {

        "ta",
        "tamil" ->
            "🌐 Tamil"

        "hi",
        "hindi" ->
            "🌐 Hindi"

        "te",
        "telugu" ->
            "🌐 Telugu"

        "kn",
        "kannada" ->
            "🌐 Kannada"

        else ->
            "🌐 English"
    }
}
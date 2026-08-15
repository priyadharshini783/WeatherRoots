package com.example.weatherroots.ui.voiceassistant
import androidx.compose.ui.graphics.graphicsLayer
import com.example.weatherroots.ui.voiceassistant.VoiceResponseEngine
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.foundation.background



@Composable
fun VoiceAssistantScreen(

    onNavigateBack: () -> Unit

) {
    var assistantResponse by remember {

        mutableStateOf("")

    }


    val context = LocalContext.current
    val infiniteTransition =
        rememberInfiniteTransition(
            label = "logoAnimation"
        )


    val scaleAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis = 1200,
                        easing = FastOutSlowInEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label = "scale"
    )



    var selectedLanguage by remember {

        mutableStateOf("English")

    }



    var expanded by remember {

        mutableStateOf(false)

    }



    var question by remember {

        mutableStateOf("")

    }



    var response by remember {

        mutableStateOf("")

    }





    val speechManager =
        remember {

            SpeechRecognizerManager(context)

        }



    val ttsManager =
        remember {

            TextToSpeechManager(context)

        }



    val translationManager =
        remember {

            TranslationManager()

        }



    val scope =
        rememberCoroutineScope()





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

        ){

            hasPermission = it

        }






    DisposableEffect(Unit){


        onDispose {


            speechManager.stopListening()

            ttsManager.shutdown()

        }

    }







    Column(


        modifier = Modifier

            .fillMaxSize()

            .background(

                Color(0xFFF4FFF5)

            )

            .padding(20.dp)



    ){



        // HEADER


        Card(


            modifier = Modifier.fillMaxWidth(),


            shape = RoundedCornerShape(25.dp),


            colors = CardDefaults.cardColors(

                containerColor =
                    Color(0xFF2E7D32)

            )


        ){


            Column(

                modifier =
                    Modifier.padding(20.dp)

            ){


                Column(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ){

                    Text(

                        text = "🌱",

                        fontSize = 55.sp,

                        modifier =
                            Modifier
                                .graphicsLayer {

                                    scaleX =
                                        scaleAnimation

                                    scaleY =
                                        scaleAnimation

                                }

                    )


                    Spacer(
                        Modifier.height(8.dp)
                    )


                    Text(

                        text = "WeatherRoots AI",

                        fontSize = 30.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF2E7D32)

                    )


                    Spacer(
                        Modifier.height(6.dp)
                    )


                    Text(

                        text =
                            "Ask farming questions\nin your own language",

                        fontSize = 16.sp,

                        color =
                            Color.Gray,

                        textAlign =
                            androidx.compose.ui.text.style.TextAlign.Center

                    )

                }


                Spacer(

                    Modifier.height(8.dp)

                )


                Text(

                    text =
                        "Your smart farming assistant",

                    color = Color.White,

                    fontSize = 16.sp

                )


            }


        }







        Spacer(

            Modifier.height(25.dp)

        )






        Text(

            text = "🌐 Select Farmer Language",

            fontSize = 18.sp,

            fontWeight = FontWeight.Bold

        )






        Spacer(

            Modifier.height(10.dp)

        )






        Box {


            Button(


                onClick = {


                    expanded = true


                },


                shape =
                    RoundedCornerShape(15.dp)

            ){


                Text(selectedLanguage)


            }






            DropdownMenu(

                expanded = expanded,


                onDismissRequest = {


                    expanded = false


                }


            ){



                listOf(

                    "English",

                    "Tamil",

                    "Hindi",

                    "Telugu",

                    "Kannada"

                ).forEach { language ->




                    DropdownMenuItem(


                        text = {


                            Text(language)


                        },


                        onClick = {


                            selectedLanguage = language

                            expanded = false


                        }


                    )


                }


            }


        }







        Spacer(

            Modifier.height(25.dp)

        )








        // VOICE BUTTON


        Button(


            onClick = {


                if(hasPermission){



                    speechManager.startListening(

                        selectedLanguage

                    ){


                            nativeText ->



                        scope.launch {



                            val englishText =


                                translationManager

                                    .translateToEnglish(

                                        nativeText,

                                        selectedLanguage

                                    )



                            question = nativeText



                        }



                    }



                }

                else{


                    permissionLauncher.launch(

                        Manifest.permission.RECORD_AUDIO

                    )


                }


            },



            modifier = Modifier

                .fillMaxWidth()

                .height(80.dp),


            shape =
                RoundedCornerShape(25.dp)



        ){


            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ){


                Text(

                    "🎤 Speak Now",

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold

                )


                Text(

                    "Ask your farming question"

                )


            }


        }







        Spacer(

            Modifier.height(20.dp)

        )







        OutlinedTextField(


            value = question,


            onValueChange = {


                question = it


            },


            label = {


                Text(

                    "Your Question (English)"

                )


            },


            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(18.dp)



        )







        Spacer(

            Modifier.height(20.dp)

        )







        Button(


            onClick = {


                response =
                    VoiceResponseEngine.getResponse(question)


            },


            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(20.dp)


        ){


            Text(

                "🤖 Ask AI",

                fontSize = 18.sp

            )


        }







        Spacer(

            Modifier.height(20.dp)

        )







        Card(


            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(22.dp),



            colors =
                CardDefaults.cardColors(

                    containerColor = Color.White

                )



        ){



            Column(


                modifier =
                    Modifier.padding(20.dp)



            ){



                Text(

                    "🌱 AI Farming Advice",

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold

                )




                Spacer(

                    Modifier.height(10.dp)

                )




                Text(

                    response

                )


            }



        }







        Spacer(

            Modifier.height(20.dp)

        )








        Button(



            onClick = {


                if(response.isNotEmpty()){



                    scope.launch {
                        val voiceResponse =
                            translationManager.translateFromEnglish(
                                response,
                                selectedLanguage
                            )








                        ttsManager.speak(
                            response,

                            selectedLanguage

                        )



                    }


                }


            },



            modifier = Modifier

                .fillMaxWidth()

                .height(60.dp),


            shape =
                RoundedCornerShape(20.dp)



        ){



            Text(

                "🔊 Play Voice Response",

                fontSize = 18.sp

            )


        }



    }



}
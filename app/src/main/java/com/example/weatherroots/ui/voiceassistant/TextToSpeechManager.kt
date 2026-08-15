package com.example.weatherroots.ui.voiceassistant


import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale


class TextToSpeechManager(
    context: Context
) {


    private var textToSpeech: TextToSpeech? = null


    private var isInitialized = false



    init {


        textToSpeech =
            TextToSpeech(context) { status ->


                if(status == TextToSpeech.SUCCESS){


                    isInitialized = true


                    textToSpeech?.language =
                        Locale.ENGLISH


                    Log.d(
                        "TTS",
                        "Initialized successfully"
                    )


                }
                else{


                    Log.e(
                        "TTS",
                        "Initialization failed"
                    )

                }


            }


    }




    fun speak(
        text:String,
        language:String
    ){


        if(!isInitialized){

            Log.e(
                "TTS",
                "Not initialized yet"
            )

            return

        }



        val locale =

            when(language){


                "Tamil" ->

                    Locale(
                        "ta",
                        "IN"
                    )


                "Hindi" ->

                    Locale(
                        "hi",
                        "IN"
                    )


                else ->

                    Locale.ENGLISH

            }



        val result =
            textToSpeech?.setLanguage(locale)



        if(result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED){


            Log.e(
                "TTS",
                "Language not supported"
            )

            return

        }



        textToSpeech?.speak(

            text,

            TextToSpeech.QUEUE_FLUSH,

            null,

            "WeatherRoots_Response"

        )


    }




    fun shutdown(){

        textToSpeech?.stop()

        textToSpeech?.shutdown()

    }

}
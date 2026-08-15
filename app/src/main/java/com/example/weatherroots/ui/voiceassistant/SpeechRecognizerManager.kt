package com.example.weatherroots.ui.voiceassistant


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale



class SpeechRecognizerManager(
    private val context: Context
) {



    private var speechRecognizer: SpeechRecognizer? = null





    fun startListening(

        language: String,

        onResult: (String) -> Unit

    ) {



        // Remove previous recognizer

        speechRecognizer?.destroy()





        if (!SpeechRecognizer.isRecognitionAvailable(context)) {


            Log.e(
                "Speech",
                "Speech recognition unavailable"
            )


            return

        }






        speechRecognizer =

            SpeechRecognizer
                .createSpeechRecognizer(context)








        speechRecognizer?.setRecognitionListener(


            object : RecognitionListener {



                override fun onReadyForSpeech(
                    params: Bundle?
                ) {


                    Log.d(
                        "Speech",
                        "Ready"
                    )


                }






                override fun onBeginningOfSpeech() {


                    Log.d(
                        "Speech",
                        "Listening started"
                    )


                }






                override fun onResults(
                    results: Bundle?
                ) {



                    val matches =

                        results?.getStringArrayList(

                            SpeechRecognizer.RESULTS_RECOGNITION

                        )





                    if (!matches.isNullOrEmpty()) {


                        val text = matches[0]



                        Log.d(
                            "Speech Result",
                            text
                        )



                        onResult(text)


                    }



                }







                override fun onError(
                    error: Int
                ) {


                    val message =

                        when(error) {



                            SpeechRecognizer.ERROR_AUDIO ->
                                "Audio recording error"



                            SpeechRecognizer.ERROR_CLIENT ->
                                "Client error"



                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                                "Microphone permission missing"



                            SpeechRecognizer.ERROR_NETWORK ->
                                "Network error"



                            SpeechRecognizer.ERROR_NO_MATCH ->
                                "No speech detected"



                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                                "Speech timeout"



                            else ->
                                "Unknown error"



                        }




                    Log.e(
                        "Speech Error",
                        message
                    )



                }






                override fun onRmsChanged(
                    rmsdB: Float
                ) {}




                override fun onBufferReceived(
                    buffer: ByteArray?
                ) {}





                override fun onEndOfSpeech() {


                    Log.d(
                        "Speech",
                        "Speech finished"
                    )


                }





                override fun onPartialResults(
                    partialResults: Bundle?
                ) {}





                override fun onEvent(
                    eventType: Int,
                    params: Bundle?
                ) {}



            }


        )








        // Create speech intent

        val intent =

            Intent(

                RecognizerIntent.ACTION_RECOGNIZE_SPEECH

            )







        intent.putExtra(

            RecognizerIntent.EXTRA_LANGUAGE_MODEL,

            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM

        )







        // Selected farmer language

        val locale = when(language) {



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



            "Telugu" ->

                Locale(
                    "te",
                    "IN"
                )



            "Kannada" ->

                Locale(
                    "kn",
                    "IN"
                )



            else ->

                Locale(
                    "en",
                    "IN"
                )


        }







        intent.putExtra(

            RecognizerIntent.EXTRA_LANGUAGE,

            locale.toLanguageTag()

        )






        intent.putExtra(

            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,

            locale.toLanguageTag()

        )







        intent.putExtra(

            RecognizerIntent.EXTRA_MAX_RESULTS,

            1

        )






        intent.putExtra(

            RecognizerIntent.EXTRA_PARTIAL_RESULTS,

            true

        )







        // Use online recognition for better Indian language support

        intent.putExtra(

            RecognizerIntent.EXTRA_PREFER_OFFLINE,

            false

        )







        speechRecognizer?.startListening(intent)



    }







    fun stopListening(){



        speechRecognizer?.stopListening()



        speechRecognizer?.destroy()



        speechRecognizer = null



    }



}
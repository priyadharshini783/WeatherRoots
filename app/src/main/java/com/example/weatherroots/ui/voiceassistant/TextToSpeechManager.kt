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
            TextToSpeech(context.applicationContext) { status ->

                if (status == TextToSpeech.SUCCESS) {

                    isInitialized = true

                    textToSpeech?.language =
                        Locale.ENGLISH

                    Log.d(
                        "WeatherRootsTTS",
                        "TextToSpeech initialized successfully"
                    )

                } else {

                    isInitialized = false

                    Log.e(
                        "WeatherRootsTTS",
                        "TextToSpeech initialization failed. Status: $status"
                    )
                }
            }
    }


    // =========================================================
    // Speak text
    // =========================================================

    fun speak(
        text: String,
        language: String = "en"
    ) {

        if (text.isBlank()) {

            Log.w(
                "WeatherRootsTTS",
                "Cannot speak empty text"
            )

            return
        }


        if (!isInitialized) {

            Log.e(
                "WeatherRootsTTS",
                "TextToSpeech is not initialized yet"
            )

            return
        }


        val locale =
            getLocaleForLanguage(
                language
            )


        Log.d(
            "WeatherRootsTTS",
            "Requested language = $language, locale = $locale"
        )


        val languageResult =
            textToSpeech?.setLanguage(
                locale
            )


        if (
            languageResult ==
            TextToSpeech.LANG_MISSING_DATA
        ) {

            Log.e(
                "WeatherRootsTTS",
                "TTS language data is missing for $locale"
            )

            speakWithEnglishFallback(
                text
            )

            return
        }


        if (
            languageResult ==
            TextToSpeech.LANG_NOT_SUPPORTED
        ) {

            Log.e(
                "WeatherRootsTTS",
                "TTS language is not supported: $locale"
            )

            speakWithEnglishFallback(
                text
            )

            return
        }


        val speakResult =
            textToSpeech?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "WeatherRoots_Response"
            )


        if (
            speakResult ==
            TextToSpeech.ERROR
        ) {

            Log.e(
                "WeatherRootsTTS",
                "Failed to start speaking"
            )

        } else {

            Log.d(
                "WeatherRootsTTS",
                "Speaking response successfully"
            )
        }
    }


    // =========================================================
    // Convert backend language to Android Locale
    // =========================================================

    private fun getLocaleForLanguage(
        language: String
    ): Locale {

        return when (
            language
                .trim()
                .lowercase()
        ) {

            // Tamil
            "ta",
            "ta-in",
            "tamil" -> {

                Locale(
                    "ta",
                    "IN"
                )
            }


            // Hindi
            "hi",
            "hi-in",
            "hindi" -> {

                Locale(
                    "hi",
                    "IN"
                )
            }


            // Telugu
            "te",
            "te-in",
            "telugu" -> {

                Locale(
                    "te",
                    "IN"
                )
            }


            // English
            "en",
            "en-in",
            "english" -> {

                Locale(
                    "en",
                    "IN"
                )
            }


            // Default
            else -> {

                Log.w(
                    "WeatherRootsTTS",
                    "Unknown language '$language'. Using English."
                )

                Locale(
                    "en",
                    "IN"
                )
            }
        }
    }


    // =========================================================
    // English fallback
    // =========================================================

    private fun speakWithEnglishFallback(
        text: String
    ) {

        Log.w(
            "WeatherRootsTTS",
            "Falling back to English TTS"
        )


        val englishLocale =
            Locale(
                "en",
                "IN"
            )


        val result =
            textToSpeech?.setLanguage(
                englishLocale
            )


        if (
            result ==
            TextToSpeech.LANG_MISSING_DATA
            ||
            result ==
            TextToSpeech.LANG_NOT_SUPPORTED
        ) {

            Log.e(
                "WeatherRootsTTS",
                "English TTS is also unavailable"
            )

            return
        }


        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "WeatherRoots_English_Fallback"
        )
    }


    // =========================================================
    // Stop current speech
    // =========================================================

    fun stop() {

        if (!isInitialized) {
            return
        }


        textToSpeech?.stop()


        Log.d(
            "WeatherRootsTTS",
            "Speech stopped"
        )
    }


    // =========================================================
    // Check whether TTS is ready
    // =========================================================

    fun isReady(): Boolean {

        return isInitialized
    }


    // =========================================================
    // Release resources
    // =========================================================

    fun shutdown() {

        textToSpeech?.stop()

        textToSpeech?.shutdown()

        textToSpeech = null

        isInitialized = false


        Log.d(
            "WeatherRootsTTS",
            "TextToSpeech shutdown complete"
        )
    }
}
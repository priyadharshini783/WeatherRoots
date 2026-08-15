package com.example.weatherroots.ui.language


import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat


object AppLanguageManager {


    private const val PREF_NAME = "language_pref"

    private const val LANGUAGE_KEY = "selected_language"



    // For Compose UI refresh
    var currentLanguage = mutableStateOf("English")



    fun setLanguage(
        context: Context,
        languageCode: String
    ) {


        // Apply Android locale
        val localeList =
            LocaleListCompat.forLanguageTags(
                languageCode
            )


        AppCompatDelegate.setApplicationLocales(
            localeList
        )



        // Update Compose state
        currentLanguage.value =
            when(languageCode) {

                "ta" -> "Tamil"

                "hi" -> "Hindi"

                "te" -> "Telugu"

                else -> "English"

            }



        saveLanguage(
            context,
            languageCode
        )


    }





    fun getLanguage(
        context: Context
    ): String {


        val languageCode =
            context
                .getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                )
                .getString(
                    LANGUAGE_KEY,
                    "en"
                ) ?: "en"



        currentLanguage.value =
            when(languageCode) {

                "ta" -> "Tamil"

                "hi" -> "Hindi"

                "te" -> "Telugu"

                else -> "English"

            }



        return languageCode

    }





    private fun saveLanguage(
        context: Context,
        languageCode: String
    ) {


        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                LANGUAGE_KEY,
                languageCode
            )
            .apply()


    }


}
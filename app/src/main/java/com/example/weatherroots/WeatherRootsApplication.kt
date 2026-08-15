package com.example.weatherroots


import android.app.Application
import com.example.weatherroots.ui.language.AppLanguageManager



class WeatherRootsApplication : Application() {


    override fun onCreate() {

        super.onCreate()


        val language =
            AppLanguageManager.getLanguage(
                this
            )


        AppLanguageManager.setLanguage(
            this,
            language
        )


    }


}
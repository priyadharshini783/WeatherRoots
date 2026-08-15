package com.example.weatherroots

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class WeatherRootsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val app = FirebaseApp.initializeApp(this)

        if(app != null){
            Log.d("FirebaseCheck","Firebase initialized")
        }
        else{
            Log.e("FirebaseCheck","Firebase initialization failed")
        }

    }
}
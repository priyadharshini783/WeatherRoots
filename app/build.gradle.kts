import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.gms.google-services")

    kotlin("kapt")
}


val secretsProperties = Properties().apply {

    val secretsFile =
        rootProject.file("secrets.properties")

    if (secretsFile.exists()) {

        secretsFile.inputStream().use { inputStream ->
            load(inputStream)
        }
    }
}


val openWeatherApiKey =
    secretsProperties.getProperty(
        "OPENWEATHER_API_KEY",
        ""
    )


android {

    namespace =
        "com.example.weatherroots"

    compileSdk {
        version = release(36)
    }


    defaultConfig {

        applicationId =
            "com.example.weatherroots"

        minSdk = 24

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            "\"$openWeatherApiKey\""
        )
    }


    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }


    kotlinOptions {

        jvmTarget = "11"
    }


    buildFeatures {

        compose = true

        buildConfig = true
    }
}


dependencies {

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.appcompat
    )


    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.compose.material3
    )


    // =========================================================
    // LOCATION
    // =========================================================

    implementation(
        libs.google.play.services.location
    )


    // =========================================================
    // FIREBASE
    // =========================================================

    implementation(
        platform(
            "com.google.firebase:firebase-bom:33.7.0"
        )
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )

    implementation(
        "com.google.firebase:firebase-firestore"
    )


    // =========================================================
    // ML KIT TRANSLATION
    // =========================================================

    implementation(
        "com.google.mlkit:translate:17.0.3"
    )


    // =========================================================
    // NAVIGATION
    // =========================================================

    implementation(
        libs.androidx.navigation.compose
    )


    // =========================================================
    // RETROFIT
    // =========================================================

    implementation(
        libs.retrofit
    )

    implementation(
        libs.retrofit.kotlinx.serialization
    )

    implementation(
        libs.kotlinx.serialization.json
    )

    implementation(
        "com.squareup.retrofit2:retrofit:2.11.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:2.11.0"
    )

    implementation(
        "com.squareup.okhttp3:logging-interceptor:4.12.0"
    )


    // =========================================================
    // ROOM DATABASE
    // =========================================================

    val roomVersion = "2.7.2"

    implementation(
        "androidx.room:room-runtime:$roomVersion"
    )

    implementation(
        "androidx.room:room-ktx:$roomVersion"
    )

    kapt(
        "androidx.room:room-compiler:$roomVersion"
    )


    // =========================================================
    // COROUTINES
    // =========================================================

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1"
    )


    // =========================================================
    // TESTING
    // =========================================================

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}
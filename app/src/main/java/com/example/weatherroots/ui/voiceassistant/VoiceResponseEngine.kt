package com.example.weatherroots.ui.voiceassistant


import com.example.weatherroots.ui.language.AppLanguageManager


object VoiceResponseEngine {


    fun getResponse(
        question: String
    ): String {


        // Get current selected app language
        val language =
            when(AppLanguageManager.currentLanguage.value){

                "Tamil" -> "ta"

                "Hindi" -> "hi"

                "Telugu" -> "te"

                else -> "en"

            }



        val query =
            question.lowercase()



        return when {


            // 🌦 Weather Questions

            query.contains("weather") ||
                    query.contains("rain") ||
                    query.contains("temperature") ||
                    query.contains("வானிலை") ||
                    query.contains("மழை") ||
                    query.contains("வெப்பநிலை") ||
                    query.contains("मौसम") ||
                    query.contains("बारिश") ||
                    query.contains("तापमान") ||
                    query.contains("వాతావరణం") ||
                    query.contains("వర్షం") ||
                    query.contains("ఉష్ణోగ్రత") -> {


                VoiceResponses.weatherAdvice[language]
                    ?: VoiceResponses.weatherAdvice["en"]!!

            }



            // 💧 Irrigation Questions

            query.contains("water") ||
                    query.contains("irrigation") ||
                    query.contains("நீர்") ||
                    query.contains("நீர்ப்பாசனம்") ||
                    query.contains("सिंचाई") ||
                    query.contains("पानी") ||
                    query.contains("నీరు") ||
                    query.contains("నీటిపారుదల") -> {


                VoiceResponses.irrigationAdvice[language]
                    ?: VoiceResponses.irrigationAdvice["en"]!!

            }




            // 🌱 Soil Based Crop Questions

            query.contains("soil") ||
                    query.contains("black soil") ||
                    query.contains("red soil") ||
                    query.contains("மண்") ||
                    query.contains("கருப்பு மண்") ||
                    query.contains("செம்மண்") ||
                    query.contains("मिट्टी") ||
                    query.contains("काली मिट्टी") ||
                    query.contains("నేల") ||
                    query.contains("నల్ల నేల") -> {


                VoiceResponses.soilAdvice[language]
                    ?: VoiceResponses.soilAdvice["en"]!!

            }





            // 🌾 Crop Selection Questions

            query.contains("crop") ||
                    query.contains("plant") ||
                    query.contains("grow") ||
                    query.contains("பயிர்") ||
                    query.contains("வளர்க்க") ||
                    query.contains("फसल") ||
                    query.contains("खेती") ||
                    query.contains("పంట") ||
                    query.contains("సాగు") -> {


                VoiceResponses.cropAdvice[language]
                    ?: VoiceResponses.cropAdvice["en"]!!

            }





            // 👨‍🌾 General Farming Questions

            query.contains("farm") ||
                    query.contains("farming") ||
                    query.contains("விவசாயம்") ||
                    query.contains("விவசாய") ||
                    query.contains("कृषि") ||
                    query.contains("खेती") ||
                    query.contains("వ్యవసాయం") -> {


                VoiceResponses.farmingAdvice[language]
                    ?: VoiceResponses.farmingAdvice["en"]!!

            }





            // Unknown Question

            else -> {


                when(language) {


                    "ta" ->
                        "மன்னிக்கவும், இந்த கேள்விக்கான பதில் இல்லை."


                    "hi" ->
                        "क्षमा करें, इस प्रश्न का उत्तर उपलब्ध नहीं है।"


                    "te" ->
                        "క్షమించండి, ఈ ప్రశ్నకు సమాధానం అందుబాటులో లేదు."


                    else ->
                        "Sorry, I don't have an answer for this question."

                }


            }


        }


    }


}
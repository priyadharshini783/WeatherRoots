package com.example.weatherroots.ui.language


fun appText(
    key: String
): String {


    return when (
        AppLanguageManager.currentLanguage.value
    ) {


        "Tamil" -> {

            when(key) {

                "WeatherRoots" ->
                    "வெதர்ரூட்ஸ்"

                "Good Morning Farmer 👨‍🌾" ->
                    "காலை வணக்கம் விவசாயி 👨‍🌾"

                "Weather Update" ->
                    "உங்கள் விவசாய வானிலை தகவல்"

                "Today's Conditions" ->
                    "இன்றைய நிலைகள்"

                "Humidity" ->
                    "ஈரப்பதம்"

                "Wind" ->
                    "காற்று"

                "Rainfall" ->
                    "மழைப்பொழிவு"

                "Crop Status" ->
                    "பயிர் நிலை"

                "Good" ->
                    "நன்று"

                "Farming Insight" ->
                    "விவசாய தகவல்"

                "Weather conditions are good for farming." ->
                    "விவசாயத்திற்கு வானிலை நிலை நல்லதாக உள்ளது."

                "Quick Actions" ->
                    "விரைவு செயல்கள்"

                "AI Assistant" ->
                    "AI உதவியாளர்"

                "Crop Advice" ->
                    "பயிர் ஆலோசனை"

                "Settings" ->
                    "அமைப்புகள்"

                "Live Weather Update" ->
                    "நேரடி வானிலை புதுப்பிப்பு"


                else ->
                    key
            }

        }



        "Hindi" -> {


            when(key) {


                "WeatherRoots" ->
                    "वेदररूट्स"


                "Good Morning Farmer 👨‍🌾" ->
                    "सुप्रभात किसान 👨‍🌾"


                "Weather Update" ->
                    "आपका कृषि मौसम अपडेट"


                "Today's Conditions" ->
                    "आज की स्थिति"


                "Humidity" ->
                    "नमी"


                "Wind" ->
                    "हवा"


                "Rainfall" ->
                    "वर्षा"


                "Crop Status" ->
                    "फसल स्थिति"


                "Good" ->
                    "अच्छा"


                "Farming Insight" ->
                    "कृषि जानकारी"


                "Weather conditions are good for farming." ->
                    "मौसम की स्थिति खेती के लिए अच्छी है।"


                "Quick Actions" ->
                    "त्वरित कार्य"


                "AI Assistant" ->
                    "AI सहायक"


                "Crop Advice" ->
                    "फसल सलाह"


                "Settings" ->
                    "सेटिंग्स"


                "Live Weather Update" ->
                    "लाइव मौसम अपडेट"


                else ->
                    key
            }

        }



        "Telugu" -> {


            when(key) {


                "WeatherRoots" ->
                    "వెదర్‌రూట్స్"


                "Good Morning Farmer 👨‍🌾" ->
                    "శుభోదయం రైతు 👨‍🌾"


                "Weather Update" ->
                    "మీ వ్యవసాయ వాతావరణ సమాచారం"


                "Today's Conditions" ->
                    "ఈరోజు పరిస్థితులు"


                "Humidity" ->
                    "తేమ"


                "Wind" ->
                    "గాలి"


                "Rainfall" ->
                    "వర్షపాతం"


                "Crop Status" ->
                    "పంట స్థితి"


                "Good" ->
                    "మంచిది"


                "Farming Insight" ->
                    "వ్యవసాయ సమాచారం"


                "Weather conditions are good for farming." ->
                    "వ్యవసాయానికి వాతావరణ పరిస్థితులు అనుకూలంగా ఉన్నాయి."


                "Quick Actions" ->
                    "త్వరిత చర్యలు"


                "AI Assistant" ->
                    "AI సహాయకుడు"


                "Crop Advice" ->
                    "పంట సలహా"


                "Settings" ->
                    "సెట్టింగ్స్"


                "Live Weather Update" ->
                    "ప్రత్యక్ష వాతావరణ నవీకరణ"


                else ->
                    key
            }

        }



        else -> {


            when(key) {


                "WeatherRoots" ->
                    "WeatherRoots"


                "Good Morning Farmer 👨‍🌾" ->
                    "Good Morning Farmer 👨‍🌾"


                "Weather Update" ->
                    "Here is your farming weather update"


                "Today's Conditions" ->
                    "Today's Conditions"


                "Humidity" ->
                    "Humidity"


                "Wind" ->
                    "Wind"


                "Rainfall" ->
                    "Rainfall"


                "Crop Status" ->
                    "Crop Status"


                "Good" ->
                    "Good"


                "Farming Insight" ->
                    "Farming Insight"


                "Weather conditions are good for farming." ->
                    "Weather conditions are good for farming."


                "Quick Actions" ->
                    "Quick Actions"


                "AI Assistant" ->
                    "AI Assistant"


                "Crop Advice" ->
                    "Crop Advice"


                "Settings" ->
                    "Settings"


                "Live Weather Update" ->
                    "Live Weather Update"


                else ->
                    key

            }

        }

    }

}
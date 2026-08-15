package com.example.weatherroots.ui.recommendation


data class CropRecommendationResult(

    val cropName: String,

    val reason: String,

    val advice: String

)



object CropRecommendationEngine {


    fun getRecommendation(

        soil: String,

        water: String,

        previousCrop: String,

        season: String

    ): CropRecommendationResult {



        return when {


            soil == "Clay Soil" ||
                    water == "High" ||
                    season == "Monsoon" -> {


                CropRecommendationResult(

                    cropName = "🌾 Rice",

                    reason =
                        "✓ Suitable weather condition\n" +
                                "✓ Water availability matches\n" +
                                "✓ Monsoon season is favourable",


                    advice =
                        "Maintain proper irrigation and monitor water levels."

                )


            }



            soil == "Black Soil" &&
                    season == "Summer" -> {


                CropRecommendationResult(

                    cropName = "🌱 Cotton",

                    reason =
                        "✓ Black soil is suitable\n" +
                                "✓ Warm climate supports growth\n" +
                                "✓ Moderate water requirement",


                    advice =
                        "Maintain soil moisture and control pests."

                )


            }



            soil == "Red Soil" &&
                    season == "Winter" -> {


                CropRecommendationResult(

                    cropName = "🥜 Groundnut",

                    reason =
                        "✓ Red soil supports cultivation\n" +
                                "✓ Winter climate is favourable\n" +
                                "✓ Suitable water requirement",


                    advice =
                        "Avoid excess irrigation and maintain drainage."

                )


            }



            else -> {


                CropRecommendationResult(

                    cropName = "🌽 Maize",

                    reason =
                        "✓ Suitable crop based on available conditions\n" +
                                "✓ Moderate climate support",


                    advice =
                        "Maintain regular irrigation."

                )


            }


        }

    }

}
package com.example.weatherroots.ui.voiceassistant


import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await



class TranslationManager {
    private fun getLanguageCode(
        language: String
    ): String {


        return when(language) {


            "Tamil" ->
                "ta"



            "Hindi" ->
                "hi"



            "Telugu" ->
                "te"



            "Malayalam" ->
                "ml"



            "Kannada" ->
                "kn"



            else ->
                "en"


        }

    







    }





    // Farmer Language → English

    suspend fun translateToEnglish(

        text: String,

        sourceLanguage: String

    ): String {



        if(sourceLanguage == "English"){

            return text

        }




        val options =

            TranslatorOptions.Builder()


                .setSourceLanguage(

                    getLanguageCode(
                        sourceLanguage
                    )

                )


                .setTargetLanguage(

                    TranslateLanguage.ENGLISH

                )


                .build()





        val translator =

            Translation

                .getClient(options)





        translator

            .downloadModelIfNeeded()

            .await()





        val result =

            translator

                .translate(text)

                .await()





        translator.close()



        return result


    }






    // English → Farmer Language

    suspend fun translateFromEnglish(

        text: String,

        targetLanguage: String

    ): String {



        if(targetLanguage == "English"){

            return text

        }





        val options =

            TranslatorOptions.Builder()


                .setSourceLanguage(

                    TranslateLanguage.ENGLISH

                )


                .setTargetLanguage(

                    getLanguageCode(
                        targetLanguage
                    )

                )


                .build()






        val translator =

            Translation

                .getClient(options)






        translator

            .downloadModelIfNeeded()

            .await()





        val result =

            translator

                .translate(text)

                .await()





        translator.close()





        return result


    }



}
package com.example.weatherroots.ui.settings


import com.google.firebase.auth.FirebaseAuth

import android.app.Activity

import androidx.appcompat.app.AppCompatDelegate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.res.stringResource

import androidx.core.os.LocaleListCompat

import com.example.weatherroots.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(

    onNavigateBack: () -> Unit,

    onLogout: () -> Unit,

    onOpenProfile: () -> Unit,

    modifier: Modifier = Modifier

) {


    val selectedLanguageCode =

        AppCompatDelegate
            .getApplicationLocales()
            .get(0)
            ?.language
            ?.takeIf {

                it in supportedLanguageCodes

            }
            ?: ENGLISH_LANGUAGE_CODE




    val currentUser =

        FirebaseAuth
            .getInstance()
            .currentUser



    val isLoggedIn = currentUser != null




    Scaffold(

        modifier = modifier,

        containerColor =
            MaterialTheme.colorScheme.background,


        topBar = {


            CenterAlignedTopAppBar(


                title = {


                    Text(

                        text =
                            stringResource(
                                R.string.settings
                            ),


                        fontWeight =
                            FontWeight.Bold

                    )


                },


                navigationIcon = {


                    IconButton(

                        onClick =
                            onNavigateBack

                    ){


                        Icon(

                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,


                            contentDescription =
                                stringResource(
                                    R.string.back
                                )

                        )


                    }


                }


            )


        }


    ){ innerPadding ->





        Column(


            modifier = Modifier

                .fillMaxSize()

                .padding(innerPadding)

                .verticalScroll(
                    rememberScrollState()
                )

                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),



            verticalArrangement =

                Arrangement.spacedBy(24.dp)



        ){



            SettingsHeader()



            LanguageSettingsCard(


                selectedLanguageCode =

                    selectedLanguageCode,



                onLanguageSelected = { languageCode ->

                    val localeList =
                        LocaleListCompat.forLanguageTags(
                            languageCode
                        )

                    AppCompatDelegate.setApplicationLocales(
                        localeList
                    )

                }


            )




            AccountSettingsCard(


                isLoggedIn = isLoggedIn,


                onLoginClick = {


                    // navigate later


                },


                onLogoutClick = {



                    FirebaseAuth

                        .getInstance()

                        .signOut()



                    onLogout()


                },



                onOpenProfile = {


                    onOpenProfile()


                }



            )




            AppInformationCard()



            Spacer(

                modifier =
                    Modifier.height(16.dp)

            )


        }



    }



}
@Composable
private fun SettingsHeader() {


    Column(

        modifier = Modifier
            .fillMaxWidth()

    ) {


        Text(

            text = stringResource(
                R.string.app_settings
            ),


            style = MaterialTheme
                .typography
                .headlineSmall,


            fontWeight =
                FontWeight.Bold

        )



        Spacer(

            modifier =
                Modifier.height(8.dp)

        )



        Text(

            text = stringResource(
                R.string.choose_language
            ),


            style =
                MaterialTheme
                    .typography
                    .bodyMedium

        )


    }

}
@Composable
private fun LanguageSettingsCard(

    selectedLanguageCode:String,

    onLanguageSelected:(String)->Unit

){

    Card(

        modifier =
            Modifier.fillMaxWidth()

    ){

        Column(

            modifier =
                Modifier.padding(16.dp)

        ){


            Text(

                text =
                    stringResource(
                        R.string.language
                    ),

                fontWeight =
                    FontWeight.Bold

            )


            Spacer(
                Modifier.height(12.dp)
            )



            LanguageButton(
                "English",
                ENGLISH_LANGUAGE_CODE,
                selectedLanguageCode,
                onLanguageSelected
            )


            LanguageButton(
                "தமிழ்",
                TAMIL_LANGUAGE_CODE,
                selectedLanguageCode,
                onLanguageSelected
            )


            LanguageButton(
                "हिंदी",
                HINDI_LANGUAGE_CODE,
                selectedLanguageCode,
                onLanguageSelected
            )


            LanguageButton(
                "తెలుగు",
                TELUGU_LANGUAGE_CODE,
                selectedLanguageCode,
                onLanguageSelected
            )

        }

    }

}
@Composable
private fun LanguageButton(

    name:String,

    code:String,

    selected:String,

    onClick:(String)->Unit

){

    Button(

        onClick = {

            onClick(code)

        },

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)

    ){

        Text(

            text =
                if(selected==code)

                    "✓ $name"

                else

                    name

        )

    }

}
@Composable
private fun AccountSettingsCard(

    isLoggedIn: Boolean,

    onLoginClick: () -> Unit,

    onLogoutClick: () -> Unit,

    onOpenProfile: () -> Unit

) {


    Card(

        modifier =
            Modifier.fillMaxWidth(),


        elevation =
            CardDefaults
                .cardElevation(
                    4.dp
                )

    ){


        Column(

            modifier =
                Modifier.padding(16.dp)

        ){



            Text(

                text =
                    stringResource(
                        R.string.profile
                    ),


                style =
                    MaterialTheme
                        .typography
                        .titleMedium,


                fontWeight =
                    FontWeight.Bold

            )



            Spacer(

                modifier =
                    Modifier.height(16.dp)

            )





            if(isLoggedIn){



                Button(


                    onClick = {

                        onOpenProfile()

                    },


                    modifier =
                        Modifier.fillMaxWidth()


                ){


                    Text(

                        text =
                            stringResource(
                                R.string.profile
                            )

                    )


                }




                Spacer(

                    modifier =
                        Modifier.height(12.dp)

                )






                OutlinedButton(


                    onClick = {


                        onLogoutClick()


                    },


                    modifier =
                        Modifier.fillMaxWidth()


                ){


                    Text(

                        text =
                            stringResource(
                                R.string.logout
                            )

                    )


                }





            }

            else {



                Button(


                    onClick = {

                        onLoginClick()

                    },


                    modifier =
                        Modifier.fillMaxWidth()


                ){



                    Text(

                        text =
                            stringResource(
                                R.string.login
                            )

                    )


                }



            }




        }


    }



}

@Composable
private fun AppInformationCard() {


    Card(

        modifier =
            Modifier.fillMaxWidth(),


        elevation =
            CardDefaults
                .cardElevation(
                    4.dp
                )

    ){


        Column(

            modifier =
                Modifier.padding(16.dp)

        ){



            Text(

                text =
                    stringResource(
                        R.string.about
                    ),


                style =
                    MaterialTheme
                        .typography
                        .titleMedium,


                fontWeight =
                    FontWeight.Bold

            )



            Spacer(

                modifier =
                    Modifier.height(12.dp)

            )



            Text(

                text =
                    stringResource(
                        R.string.app_description
                    ),


                style =
                    MaterialTheme
                        .typography
                        .bodyMedium

            )



            Spacer(

                modifier =
                    Modifier.height(12.dp)

            )



            Text(

                text =
                    stringResource(
                        R.string.version
                    ) + " 1.0.0"

            )



        }


    }


}
private const val ENGLISH_LANGUAGE_CODE = "en"

private const val TAMIL_LANGUAGE_CODE = "ta"

private const val HINDI_LANGUAGE_CODE = "hi"

private const val TELUGU_LANGUAGE_CODE = "te"


private val supportedLanguageCodes = setOf(

    ENGLISH_LANGUAGE_CODE,

    TAMIL_LANGUAGE_CODE,

    HINDI_LANGUAGE_CODE,

    TELUGU_LANGUAGE_CODE

)
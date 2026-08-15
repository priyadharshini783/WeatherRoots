package com.example.weatherroots.ui.auth


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(

    onUserLoggedIn: () -> Unit,

    onUserNotLoggedIn: () -> Unit

) {


    val auth = FirebaseAuth.getInstance()



    LaunchedEffect(Unit) {


        delay(2000)


        val currentUser = auth.currentUser



        if(currentUser != null){

            onUserLoggedIn()

        }
        else{

            onUserNotLoggedIn()

        }

    }




    Box(

        modifier = Modifier.fillMaxSize(),

        contentAlignment = Alignment.Center

    ){


        Column(

            horizontalAlignment = Alignment.CenterHorizontally

        ){

            Text(

                text = "🌱 WeatherRoots",

                fontSize = 32.sp

            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            CircularProgressIndicator()


        }


    }

}
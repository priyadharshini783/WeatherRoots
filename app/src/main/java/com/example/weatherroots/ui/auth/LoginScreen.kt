package com.example.weatherroots.ui.auth


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun LoginScreen(

    viewModel: AuthViewModel,

    onLoginSuccess: () -> Unit,

    onNavigateToSignup: () -> Unit,

    modifier: Modifier = Modifier

) {


    var email by remember {

        mutableStateOf("")

    }


    var password by remember {

        mutableStateOf("")

    }



    val uiState by viewModel.uiState.collectAsState()



    LaunchedEffect(uiState) {

        if(uiState is AuthUiState.Success){

            onLoginSuccess()

        }

    }




    Box(

        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),

        contentAlignment = Alignment.Center

    ){



        Column(

            modifier = Modifier.fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ){



            Text(

                text = "🌱 WeatherRoots",

                fontSize = 32.sp,

                fontWeight = FontWeight.Bold,

                color = MaterialTheme.colorScheme.primary

            )



            Text(

                text = "Farmer Login",

                fontSize = 22.sp,

                fontWeight = FontWeight.SemiBold

            )





            OutlinedTextField(

                value = email,

                onValueChange = {

                    email = it

                },

                label = {

                    Text("Email")

                },

                modifier = Modifier.fillMaxWidth()

            )





            OutlinedTextField(

                value = password,

                onValueChange = {

                    password = it

                },


                label = {

                    Text("Password")

                },


                visualTransformation =
                    PasswordVisualTransformation(),


                modifier = Modifier.fillMaxWidth()

            )





            Button(

                onClick = {


                    viewModel.login(

                        email,

                        password

                    )


                },


                modifier = Modifier

                    .fillMaxWidth()

                    .height(55.dp),


                shape = RoundedCornerShape(14.dp)

            ){


                if(uiState is AuthUiState.Loading){


                    CircularProgressIndicator(

                        modifier = Modifier.size(24.dp),

                        color = MaterialTheme.colorScheme.onPrimary

                    )


                }

                else{


                    Text(

                        "Login",

                        fontSize = 18.sp,

                        fontWeight = FontWeight.Bold

                    )


                }


            }





            TextButton(

                onClick = onNavigateToSignup

            ){

                Text(

                    "Don't have an account? Create Account"

                )

            }




            if(uiState is AuthUiState.Error){


                Text(

                    text = (uiState as AuthUiState.Error).message,

                    color = MaterialTheme.colorScheme.error

                )


            }



        }


    }


}
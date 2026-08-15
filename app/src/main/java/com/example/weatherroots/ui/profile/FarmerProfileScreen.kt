package com.example.weatherroots.ui.profile

import com.example.weatherroots.domain.model.FarmerProfile
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun FarmerProfileScreen(

    viewModel: FarmerProfileViewModel,

    onProfileSaved: () -> Unit

) {


    var farmerName by remember {
        mutableStateOf("")
    }


    var phone by remember {
        mutableStateOf("")
    }


    var location by remember {
        mutableStateOf("")
    }


    var language by remember {
        mutableStateOf("Tamil")
    }


    var farmSize by remember {
        mutableStateOf("")
    }


    var cropType by remember {
        mutableStateOf("")
    }



    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(
                rememberScrollState()
            )

    ) {


        Text(

            text = "👨‍🌾 Farmer Profile",

            fontSize = 28.sp

        )


        Spacer(
            modifier = Modifier.height(25.dp)
        )



        OutlinedTextField(

            value = farmerName,

            onValueChange = {
                farmerName = it
            },

            label = {
                Text("Farmer Name")
            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(15.dp)
        )



        OutlinedTextField(

            value = phone,

            onValueChange = {
                phone = it
            },

            label = {
                Text("Phone Number")
            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(15.dp)
        )



        OutlinedTextField(

            value = location,

            onValueChange = {
                location = it
            },

            label = {
                Text("Village / Location")
            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(15.dp)
        )



        OutlinedTextField(

            value = farmSize,

            onValueChange = {
                farmSize = it
            },

            label = {
                Text("Farm Size (acres)")
            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(15.dp)
        )



        OutlinedTextField(

            value = cropType,

            onValueChange = {
                cropType = it
            },

            label = {
                Text("Main Crop")
            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(25.dp)
        )


        Button(

            onClick = {


                val profile = FarmerProfile(

                    name = farmerName,

                    phone = phone,

                    location = location,

                    language = language,

                    farmSize = farmSize,

                    cropType = cropType

                )


                viewModel.saveProfile(

                    profile = profile,

                    onSuccess = {

                        onProfileSaved()

                    }

                )


            },

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)

        )
        {

            Text("Save Profile")

        }


    }

}
package com.example.weatherroots.data.repository


import com.example.weatherroots.data.remote.FirebaseAuthService



class AuthRepository(

    private val authService: FirebaseAuthService

) {



    suspend fun login(
        email:String,
        password:String
    ):Result<Unit>{


        return authService.login(
            email,
            password
        )

    }




    suspend fun signup(
        email:String,
        password:String
    ):Result<Unit>{


        return authService.signup(
            email,
            password
        )

    }



    fun logout(){

        authService.logout()

    }



    fun getCurrentUser() =
        authService.currentUser()

}
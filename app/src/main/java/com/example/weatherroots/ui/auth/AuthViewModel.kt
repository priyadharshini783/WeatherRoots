package com.example.weatherroots.ui.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroots.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class AuthViewModel(

    private val repository: AuthRepository

):ViewModel(){



    private val _uiState =
        MutableStateFlow<AuthUiState>(
            AuthUiState.Idle
        )


    val uiState:StateFlow<AuthUiState> =
        _uiState





    fun login(
        email:String,
        password:String
    ){


        viewModelScope.launch {


            _uiState.value =
                AuthUiState.Loading



            val result =
                repository.login(
                    email,
                    password
                )



            _uiState.value =

                if(result.isSuccess)

                    AuthUiState.Success

                else

                    AuthUiState.Error(
                        result.exceptionOrNull()
                            ?.message
                            ?: "Login failed"
                    )

        }


    }





    fun signup(
        email:String,
        password:String
    ){


        viewModelScope.launch {


            _uiState.value =
                AuthUiState.Loading



            val result =
                repository.signup(
                    email,
                    password
                )



            _uiState.value =

                if(result.isSuccess)

                    AuthUiState.Success

                else

                    AuthUiState.Error(
                        result.exceptionOrNull()
                            ?.message
                            ?: "Signup failed"
                    )

        }

    }




    fun logout(){

        repository.logout()

    }


}
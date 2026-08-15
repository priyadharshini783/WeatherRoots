package com.example.weatherroots.ui.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherroots.data.repository.FarmerProfileRepository
import com.example.weatherroots.domain.model.FarmerProfile
import kotlinx.coroutines.launch


class FarmerProfileViewModel(
    private val repository: FarmerProfileRepository
): ViewModel(){



    fun saveProfile(
        profile: FarmerProfile,
        onSuccess: () -> Unit
    ){

        viewModelScope.launch {


            val result =
                repository.saveProfile(profile)


            if(result.isSuccess){

                onSuccess()

            }

        }

    }

}
package com.example.weatherroots.ui.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherroots.data.repository.FarmerProfileRepository


class FarmerProfileViewModelFactory(
    private val repository: FarmerProfileRepository
): ViewModelProvider.Factory {


    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {


        if(modelClass.isAssignableFrom(
                FarmerProfileViewModel::class.java
            )){


            return FarmerProfileViewModel(
                repository
            ) as T

        }


        throw IllegalArgumentException(
            "Unknown ViewModel"
        )

    }

}
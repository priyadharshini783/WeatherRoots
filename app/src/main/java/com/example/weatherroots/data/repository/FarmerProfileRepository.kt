package com.example.weatherroots.data.repository


import com.example.weatherroots.domain.model.FarmerProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



class FarmerProfileRepository {


    private val firestore =
        FirebaseFirestore.getInstance()


    private val auth =
        FirebaseAuth.getInstance()



    suspend fun saveProfile(
        profile: FarmerProfile
    ): Result<Unit> {


        return try {


            val uid =
                auth.currentUser?.uid
                    ?: throw Exception("User not logged in")


            firestore
                .collection("users")
                .document(uid)
                .set(profile)
                .await()



            Result.success(Unit)



        } catch(e: Exception) {


            Result.failure(e)


        }


    }

}
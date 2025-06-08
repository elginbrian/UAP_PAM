package com.elginbrian.uappam.data.repositoty

import com.elginbrian.uappam.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun loginUser(email: String, pass: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    suspend fun registerUser(email: String, pass: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    fun getCurrentUser() = firebaseAuth.currentUser

    fun logout() = firebaseAuth.signOut()

}
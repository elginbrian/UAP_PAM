package com.elginbrian.uappam.data.repositoty

import com.elginbrian.uappam.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun loginUser(email: String, pass: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            Resource.Success(result)
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Format email tidak valid."
                "ERROR_WRONG_PASSWORD" -> "Password salah. Silakan coba lagi."
                "ERROR_USER_NOT_FOUND" -> "Pengguna dengan email ini tidak ditemukan."
                "ERROR_USER_DISABLED" -> "Akun pengguna ini telah dinonaktifkan."
                else -> "Login gagal. Silakan periksa kembali email dan password Anda."
            }
            Resource.Error(errorMessage)
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan yang tidak diketahui. Silakan coba lagi.")
        }
    }

    suspend fun registerUser(email: String, pass: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            Resource.Success(result)
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Alamat email ini sudah digunakan oleh akun lain."
                "ERROR_INVALID_EMAIL" -> "Format email tidak valid."
                "ERROR_WEAK_PASSWORD" -> "Password terlalu lemah. Harap gunakan minimal 6 karakter."
                else -> "Registrasi gagal. Silakan coba lagi."
            }
            Resource.Error(errorMessage)
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan yang tidak diketahui. Silakan coba lagi.")
        }
    }

    fun getCurrentUser() = firebaseAuth.currentUser

    fun logout() = firebaseAuth.signOut()

}
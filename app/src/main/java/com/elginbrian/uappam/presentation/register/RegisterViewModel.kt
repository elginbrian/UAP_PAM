package com.elginbrian.uappam.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elginbrian.uappam.data.repositoty.AuthRepository
import com.elginbrian.uappam.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<AuthResult>>()
    val registerStatus: LiveData<Resource<AuthResult>> = _registerStatus

    fun register(email: String, pass: String, confirmPass: String) {
        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            _registerStatus.postValue(Resource.Error("Semua kolom harus diisi."))
            return
        }
        if (pass != confirmPass) {
            _registerStatus.postValue(Resource.Error("Password dan konfirmasi password tidak cocok."))
            return
        }

        viewModelScope.launch {
            _registerStatus.postValue(Resource.Loading())
            val result = authRepository.registerUser(email, pass)
            _registerStatus.postValue(result)
        }
    }
}
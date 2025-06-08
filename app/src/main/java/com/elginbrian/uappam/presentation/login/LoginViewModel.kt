package com.elginbrian.uappam.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elginbrian.uappam.data.repositoty.AuthRepository
import com.elginbrian.uappam.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<Resource<AuthResult>>()
    val loginStatus: LiveData<Resource<AuthResult>> = _loginStatus

    fun login(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            _loginStatus.postValue(Resource.Error("Email dan password tidak boleh kosong."))
            return
        }

        viewModelScope.launch {
            _loginStatus.postValue(Resource.Loading())
            val result = authRepository.loginUser(email, pass)
            _loginStatus.postValue(result)
        }
    }
}
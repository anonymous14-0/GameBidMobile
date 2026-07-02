package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var loginMessage by mutableStateOf("")
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var token by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true

                val response = repository.login(
                    email,
                    password
                )

                loginMessage = response.message
                loginSuccess = response.status
                token = response.token

            } catch (e: Exception) {
                loginMessage = e.message ?: "Login gagal"
                loginSuccess = false
            } finally {
                isLoading = false
            }
        }
    }
}
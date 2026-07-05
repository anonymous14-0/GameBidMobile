package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var loginMessage by mutableStateOf("")
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var token by mutableStateOf("")
        private set

    var role by mutableStateOf("")
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

                val response = repository.login(email, password)

                if (response.user.role == "admin") {
                    loginSuccess = false
                    loginMessage = "Admin hanya bisa login via web"
                    return@launch
                }

                loginMessage = response.message
                loginSuccess = response.status
                token = response.token
                role = response.user.role

            } catch (e: HttpException) {
                loginSuccess = false

                loginMessage = when (e.code()) {
                    401 -> "Email atau password salah"
                    422 -> "Data login tidak valid"
                    500 -> "Server error"
                    else -> "Login gagal"
                }

            } catch (e: Exception) {
                loginSuccess = false
                loginMessage = "Tidak dapat terhubung ke server"
            } finally {
                isLoading = false
            }
        }
    }
    fun resetLoginState() {
        loginSuccess = false
        loginMessage = ""
        token = ""
        role = ""
    }
}
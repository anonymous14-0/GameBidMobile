package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.UserModel
import com.jemi.gamebidmobile.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    var user by mutableStateOf<UserModel?>(null)
        private set

    var errorMessage by mutableStateOf("")
        private set

    fun loadProfile(token: String) {
        viewModelScope.launch {
            try {
                user =
                    repository
                        .getProfile(token)
                        .data
            } catch (e: Exception) {
                errorMessage =
                    "Gagal load profile: ${e.message}"
            }
        }
    }
}
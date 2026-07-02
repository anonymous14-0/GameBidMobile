package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.model.LoginRequest
import com.jemi.gamebidmobile.data.remote.RetrofitClient

class AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ) = RetrofitClient.api.login(
        LoginRequest(email, password)
    )
}
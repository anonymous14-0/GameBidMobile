package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.remote.RetrofitClient

class ProfileRepository {

    suspend fun getProfile(token: String) =
        RetrofitClient.api.getProfile(
            "Bearer $token"
        )
}
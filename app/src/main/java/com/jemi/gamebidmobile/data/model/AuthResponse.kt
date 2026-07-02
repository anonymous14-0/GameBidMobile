package com.jemi.gamebidmobile.data.model

data class AuthResponse(
    val status: Boolean,
    val message: String,
    val token: String,
    val user: UserModel
)
package com.jemi.gamebidmobile.data.model

data class SendAccountRequest(
    val account_email: String,
    val account_password: String,
    val seller_note: String?
)
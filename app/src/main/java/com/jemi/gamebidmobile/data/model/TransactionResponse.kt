package com.jemi.gamebidmobile.data.model

data class TransactionResponse(
    val status: Boolean,
    val data: List<TransactionModel>
)
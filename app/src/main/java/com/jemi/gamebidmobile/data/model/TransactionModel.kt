package com.jemi.gamebidmobile.data.model

data class TransactionModel(
    val id: Int,
    val auction_id: Int,
    val buyer_id: Int,
    val amount: Double,
    val status: String,
    val payment_proof: String?,
    val account_email: String?,
    val account_password: String?,
    val seller_note: String?,
    val auction: AuctionModel? = null
)

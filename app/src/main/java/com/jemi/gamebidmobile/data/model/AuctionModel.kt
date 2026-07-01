package com.jemi.gamebidmobile.data.model

data class AuctionModel(
    val id: Int,
    val current_price: Int,
    val status: String,
    val item: ItemModel
)
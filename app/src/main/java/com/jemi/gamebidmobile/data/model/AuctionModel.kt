package com.jemi.gamebidmobile.data.model

data class AuctionModel(
    val id: Int,
    val current_price: Int,
    val status: String,
    val start_time: String,
    val end_time: String,
    val item: ItemModel,
    val bids: List<BidModel> = emptyList()
)
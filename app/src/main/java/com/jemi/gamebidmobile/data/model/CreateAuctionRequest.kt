package com.jemi.gamebidmobile.data.model

data class CreateAuctionRequest(
    val item_id: Int,
    val start_time: String,
    val end_time: String
)
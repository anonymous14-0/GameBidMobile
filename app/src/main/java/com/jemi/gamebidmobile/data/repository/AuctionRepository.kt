package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.model.BidRequest
import com.jemi.gamebidmobile.data.remote.RetrofitClient

class AuctionRepository {

    suspend fun getAuctions() =
        RetrofitClient.api.getAuctions()

    suspend fun submitBid(
        token: String,
        auctionId: Int,
        bidAmount: Int
    ) {
        RetrofitClient.api.submitBid(
            "Bearer $token",
            auctionId,
            BidRequest(bidAmount)
        )
    }
}
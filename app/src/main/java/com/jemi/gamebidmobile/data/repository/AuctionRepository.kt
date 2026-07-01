package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.remote.RetrofitClient

class AuctionRepository {

    suspend fun getAuctions() =
        RetrofitClient.api.getAuctions()
}
package com.jemi.gamebidmobile.data.remote

import com.jemi.gamebidmobile.data.model.AuctionModel
import retrofit2.http.GET
import com.jemi.gamebidmobile.data.model.AuctionResponse

interface ApiService {

    @GET("auctions")
    suspend fun getAuctions(): AuctionResponse
}
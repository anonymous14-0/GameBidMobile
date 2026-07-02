package com.jemi.gamebidmobile.data.remote

import com.jemi.gamebidmobile.data.model.AuctionResponse
import com.jemi.gamebidmobile.data.model.AuthResponse
import com.jemi.gamebidmobile.data.model.BidRequest
import com.jemi.gamebidmobile.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Header

interface ApiService {

    @GET("auctions")
    suspend fun getAuctions(): AuctionResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auctions/{id}/bid")
    suspend fun submitBid(
        @Header("Authorization") token: String,
        @Path("id") auctionId: Int,
        @Body request: BidRequest
    )
}
package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.model.BidRequest
import com.jemi.gamebidmobile.data.model.CreateAuctionRequest
import com.jemi.gamebidmobile.data.remote.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun getSellerAuctions(
        token: String
    ) =
        RetrofitClient.api.getSellerAuctions(
            "Bearer $token"
        )

    suspend fun createAuction(
        token: String,
        itemId: Int,
        startTime: String,
        endTime: String
    ) {
        RetrofitClient.api.createAuction(
            "Bearer $token",
            CreateAuctionRequest(
                itemId,
                startTime,
                endTime
            )
        )
    }

    suspend fun getSellerItems(
        token: String
    ) =
        RetrofitClient.api.getSellerItems(
            "Bearer $token"
        )

    suspend fun createItem(
        token: String,
        title: RequestBody,
        categoryId: RequestBody,
        description: RequestBody,
        startingPrice: RequestBody,
        image: MultipartBody.Part?
    ) {
        RetrofitClient.api.createItem(
            "Bearer $token",
            title,
            categoryId,
            description,
            startingPrice,
            image
        )
    }
    suspend fun getCategories(
        token: String
    ) =
        RetrofitClient.api.getCategories(
            "Bearer $token"
        )
    suspend fun getAuctionDetail(
        auctionId: Int
    ) =
        RetrofitClient.api.getAuctionDetail(
            auctionId
        )
}
package com.jemi.gamebidmobile.data.remote

import com.jemi.gamebidmobile.data.model.AuctionResponse
import com.jemi.gamebidmobile.data.model.AuthResponse
import com.jemi.gamebidmobile.data.model.BidRequest
import com.jemi.gamebidmobile.data.model.LoginRequest
import com.jemi.gamebidmobile.data.model.TransactionResponse
import com.jemi.gamebidmobile.data.model.SendAccountRequest
import com.jemi.gamebidmobile.data.model.TransactionDetailResponse
import com.jemi.gamebidmobile.data.model.CreateAuctionRequest
import com.jemi.gamebidmobile.data.model.ItemResponse
import com.jemi.gamebidmobile.data.model.CategoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Header
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part

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

    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String
    ): TransactionResponse


    @Multipart
    @POST("transactions/{id}/upload-proof")
    suspend fun uploadProof(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int,
        @Part paymentProof: MultipartBody.Part
    )

    @POST("transactions/{id}/send-account")
    suspend fun sendAccount(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int,
        @Body request: SendAccountRequest
    )

    @POST("transactions/{id}/complete")
    suspend fun completeTransaction(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    )

    @GET("transactions/{id}")
    suspend fun getTransactionDetail(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    ): TransactionDetailResponse

    @GET("seller/auctions")
    suspend fun getSellerAuctions(
        @Header("Authorization") token: String
    ): AuctionResponse

    @POST("seller/auctions")
    suspend fun createAuction(
        @Header("Authorization") token: String,
        @Body request: CreateAuctionRequest
    )

    @GET("seller/items")
    suspend fun getSellerItems(
        @Header("Authorization") token: String
    ): ItemResponse

    @Multipart
    @POST("seller/items")
    suspend fun createItem(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("description") description: RequestBody,
        @Part("starting_price") startingPrice: RequestBody,
        @Part image: MultipartBody.Part?
    )

    @GET("seller/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): CategoryResponse
}
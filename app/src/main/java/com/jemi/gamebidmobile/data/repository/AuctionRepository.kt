/*
 * File: AuctionRepository.kt
 * Fungsi: Layer Repository yang mengabstraksi pemanggilan Retrofit API. Repository dipanggil ViewModel agar UI tidak berkomunikasi langsung dengan backend Laravel.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.model.BidRequest
import com.jemi.gamebidmobile.data.model.CreateAuctionRequest
import com.jemi.gamebidmobile.data.remote.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
// Repository ini berperan sebagai adapter antara ViewModel dan ApiService.
// Semua token dari UI/ViewModel diubah menjadi header Bearer di sini agar format autentikasi konsisten.
class AuctionRepository {

    // Memanggil endpoint daftar auction publik untuk buyer; tidak memerlukan token karena data bersifat katalog aktif.
    suspend fun getAuctions() =
        RetrofitClient.api.getAuctions()

    // Mengirim bid ke endpoint auction protected dengan request body BidRequest.
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

    // Membuat auction seller dengan request JSON CreateAuctionRequest.
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

    // Membuat item seller menggunakan multipart form-data, termasuk optional image.
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
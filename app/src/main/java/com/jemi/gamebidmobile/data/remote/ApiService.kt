/*
 * File: ApiService.kt
 * Fungsi: Layer API Service/Retrofit yang mendefinisikan kontrak endpoint Laravel REST API. File ini menjadi jembatan Repository untuk melakukan request HTTP dan menerima response model.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

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
import com.jemi.gamebidmobile.data.model.AuctionDetailResponse
import com.jemi.gamebidmobile.data.model.ProfileResponse
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

    // GET /api/auctions
    // Mengambil daftar auction aktif untuk buyer; response dipetakan ke AuctionResponse.
    @GET("auctions")
    suspend fun getAuctions(): AuctionResponse

    // POST /api/login
    // Mengirim email dan password dalam LoginRequest; response berisi token, role, user, dan status login.
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    // POST /api/auctions/{id}/bid
    // Mengirim nominal bid dengan Bearer token buyer; backend memvalidasi auction dan nilai penawaran.
    @POST("auctions/{id}/bid")
    suspend fun submitBid(
        @Header("Authorization") token: String,
        @Path("id") auctionId: Int,
        @Body request: BidRequest
    )

    // GET /api/transactions
    // Mengambil daftar transaksi milik user terautentikasi berdasarkan Bearer token.
    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String
    ): TransactionResponse


    @Multipart
    // POST /api/transactions/{id}/upload-proof
    // Mengirim multipart image bukti pembayaran ke backend Laravel untuk proses escrow/transaksi.
    @POST("transactions/{id}/upload-proof")
    suspend fun uploadProof(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int,
        @Part paymentProof: MultipartBody.Part
    )

    // POST /api/transactions/{id}/send-account
    // Seller mengirim credential akun digital kepada buyer setelah pembayaran tervalidasi.
    @POST("transactions/{id}/send-account")
    suspend fun sendAccount(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int,
        @Body request: SendAccountRequest
    )

    // POST /api/transactions/{id}/complete
    // Buyer menandai transaksi selesai sehingga status escrow dapat diselesaikan di backend.
    @POST("transactions/{id}/complete")
    suspend fun completeTransaction(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    )

    // GET /api/transactions/{id}
    // Mengambil detail satu transaksi, termasuk status pembayaran, akun, dan bukti transfer.
    @GET("transactions/{id}")
    suspend fun getTransactionDetail(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    ): TransactionDetailResponse

    // GET /api/seller/auctions
    // Mengambil daftar auction milik seller yang sedang login.
    @GET("seller/auctions")
    suspend fun getSellerAuctions(
        @Header("Authorization") token: String
    ): AuctionResponse

    // POST /api/seller/auctions
    // Membuat auction dari item seller dengan waktu mulai dan selesai.
    @POST("seller/auctions")
    suspend fun createAuction(
        @Header("Authorization") token: String,
        @Body request: CreateAuctionRequest
    )

    // GET /api/seller/items
    // Mengambil master item milik seller untuk dipilih saat membuat auction.
    @GET("seller/items")
    suspend fun getSellerItems(
        @Header("Authorization") token: String
    ): ItemResponse

    @Multipart
    // POST /api/seller/items
    // Membuat item baru menggunakan multipart form-data: title, category_id, description, starting_price, dan optional image.
    @POST("seller/items")
    suspend fun createItem(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("description") description: RequestBody,
        @Part("starting_price") startingPrice: RequestBody,
        @Part image: MultipartBody.Part?
    )

    // GET /api/seller/categories
    // Mengambil daftar kategori dari backend sebagai pilihan saat seller membuat item.
    @GET("seller/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): CategoryResponse

    // GET /api/auctions/{id}
    // Mengambil detail auction, termasuk item, seller, harga, dan histori bid.
    @GET("auctions/{id}")
    suspend fun getAuctionDetail(
        @Path("id") auctionId: Int
    ): AuctionDetailResponse

    // GET /api/profile
    // Mengambil profil user dari token login yang tersimpan.
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    @Multipart
    // POST /api/profile/photo
    // Mengunggah foto profil dalam format multipart image.
    @POST("profile/photo")
    suspend fun uploadProfilePhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    )
}
/*
 * File: TransactionRepository.kt
 * Fungsi: Layer Repository yang mengabstraksi pemanggilan Retrofit API. Repository dipanggil ViewModel agar UI tidak berkomunikasi langsung dengan backend Laravel.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.remote.RetrofitClient
import com.jemi.gamebidmobile.data.model.SendAccountRequest
import okhttp3.MultipartBody

// Repository ini berperan sebagai adapter antara ViewModel dan ApiService.
// Semua token dari UI/ViewModel diubah menjadi header Bearer di sini agar format autentikasi konsisten.
class TransactionRepository {

    suspend fun getTransactions(
        token: String
    ) =
        RetrofitClient.api.getTransactions(
            "Bearer $token"
        )
    // Mengunggah bukti pembayaran multipart ke endpoint transaksi protected.
    suspend fun uploadProof(
        token: String,
        transactionId: Int,
        imagePart: MultipartBody.Part
    ) {
        RetrofitClient.api.uploadProof(
            "Bearer $token",
            transactionId,
            imagePart
        )
    }
    // Mengirim credential akun digital dalam SendAccountRequest untuk transaksi escrow.
    suspend fun sendAccount(
        token: String,
        transactionId: Int,
        email: String,
        password: String,
        note: String
    ) {
        RetrofitClient.api.sendAccount(
            "Bearer $token",
            transactionId,
            SendAccountRequest(
                email,
                password,
                note
            )
        )
    }
    // Menyelesaikan transaksi ketika buyer mengonfirmasi akun telah diterima.
    suspend fun completeTransaction(
        token: String,
        transactionId: Int
    ) {
        RetrofitClient.api.completeTransaction(
            "Bearer $token",
            transactionId
        )
    }
    suspend fun getTransactionDetail(
        token: String,
        transactionId: Int
    ) =
        RetrofitClient.api.getTransactionDetail(
            "Bearer $token",
            transactionId
        )
}
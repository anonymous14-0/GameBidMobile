/*
 * File: TransactionModel.kt
 * Fungsi: Layer Model/Data Transfer Object. File ini merepresentasikan struktur request/response JSON dari Laravel REST API agar dapat dipakai Retrofit dan ViewModel secara type-safe.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.data.model

data class TransactionModel(
    val id: Int,
    val auction_id: Int,
    val buyer_id: Int,
    val amount: Double,
    val status: String,
    val payment_proof: String?,
    val account_email: String?,
    val account_password: String?,
    val seller_note: String?,
    val auction: AuctionModel? = null
)

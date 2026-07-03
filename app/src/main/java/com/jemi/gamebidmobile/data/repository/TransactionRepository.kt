package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.remote.RetrofitClient
import com.jemi.gamebidmobile.data.model.SendAccountRequest
import okhttp3.MultipartBody

class TransactionRepository {

    suspend fun getTransactions(
        token: String
    ) =
        RetrofitClient.api.getTransactions(
            "Bearer $token"
        )
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
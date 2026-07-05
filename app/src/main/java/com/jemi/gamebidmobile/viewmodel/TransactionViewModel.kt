package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.TransactionModel
import com.jemi.gamebidmobile.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
class TransactionViewModel : ViewModel() {

    private val repository = TransactionRepository()

    var transactions by mutableStateOf<List<TransactionModel>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var uploadMessage by mutableStateOf("")
        private set
    var accountMessage by mutableStateOf("")
        private set
    var selectedTransaction by mutableStateOf<TransactionModel?>(null)
        private set

    fun uploadProof(
        token: String,
        transactionId: Int,
        imagePart: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                repository.uploadProof(
                    token,
                    transactionId,
                    imagePart
                )
                uploadMessage = "Upload berhasil"
                loadTransactionDetail(token, transactionId)
            } catch (e: Exception) {
                uploadMessage = "Upload gagal: ${e.message}"
            }
        }
    }
    fun loadTransactions(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                transactions =
                    repository
                        .getTransactions(token)
                        .data
            } catch (e: Exception) {
                uploadMessage =
                    "Gagal load transaksi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun sendAccount(
        token: String,
        transactionId: Int,
        email: String,
        password: String,
        note: String
    ) {
        viewModelScope.launch {
            try {
                repository.sendAccount(
                    token,
                    transactionId,
                    email,
                    password,
                    note
                )
                accountMessage = "Akun berhasil dikirim"
                loadTransactionDetail(token, transactionId)
            } catch (e: Exception) {
                accountMessage =
                    "Gagal kirim akun: ${e.message}"
            }
        }
    }
    fun completeTransaction(
        token: String,
        transactionId: Int
    ) {
        viewModelScope.launch {
            try {
                repository.completeTransaction(
                    token,
                    transactionId
                )
                uploadMessage = "Transaksi selesai"
            } catch (e: Exception) {
                uploadMessage =
                    "Gagal menyelesaikan transaksi"
            }
        }
    }
    fun loadTransactionDetail(
        token: String,
        transactionId: Int
    ) {
        viewModelScope.launch {
            try {
                selectedTransaction =
                    repository
                        .getTransactionDetail(
                            token,
                            transactionId
                        )
                        .data
            } catch (e: Exception) {
                uploadMessage =
                    "Gagal load detail transaksi: ${e.message}"
            }
        }
    }
}
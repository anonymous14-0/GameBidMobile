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
    var loadErrorMessage by mutableStateOf("")
        private set
    var accountMessage by mutableStateOf("")
        private set
    var selectedTransaction by mutableStateOf<TransactionModel?>(null)
        private set
    var isActionLoading by mutableStateOf(false)
        private set

    fun uploadProof(
        token: String,
        transactionId: Int,
        imagePart: MultipartBody.Part
    ) {
        viewModelScope.launch {
            isActionLoading = true
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
            } finally {
                isActionLoading = false
            }
        }
    }
    fun loadTransactions(token: String) {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                transactions =
                    repository
                        .getTransactions(token)
                        .data
            } catch (e: Exception) {
                loadErrorMessage =
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
            isActionLoading = true
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
            } finally {
                isActionLoading = false
            }
        }
    }
    fun completeTransaction(
        token: String,
        transactionId: Int
    ) {
        viewModelScope.launch {
            isActionLoading = true
            try {
                repository.completeTransaction(
                    token,
                    transactionId
                )
                uploadMessage = "Transaksi selesai"
            } catch (e: Exception) {
                uploadMessage =
                    "Gagal menyelesaikan transaksi"
            } finally {
                isActionLoading = false
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
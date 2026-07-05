/*
 * File: TransactionViewModel.kt
 * Fungsi: Layer ViewModel dalam arsitektur MVVM. File ini menyimpan state UI, menjalankan coroutine, memanggil Repository, lalu menyediakan hasilnya ke Jetpack Compose.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.TransactionModel
import com.jemi.gamebidmobile.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
// ViewModel transaksi untuk mengelola pembayaran, pengiriman akun, dan penyelesaian escrow.
// Screen transaksi membaca state dari sini, sedangkan request protected diteruskan ke Repository
// dengan Bearer token yang berasal dari TokenManager.
class TransactionViewModel : ViewModel() {

    // Repository transaksi menjadi penghubung ke endpoint Laravel /api/transactions.
    private val repository = TransactionRepository()

    // Daftar transaksi buyer/seller yang sedang login.
    var transactions by mutableStateOf<List<TransactionModel>>(emptyList())
        private set
    // Loading utama ketika daftar transaksi dimuat.
    var isLoading by mutableStateOf(false)
        private set
    // Pesan untuk proses upload bukti pembayaran atau penyelesaian transaksi.
    var uploadMessage by mutableStateOf("")
        private set
    // Pesan error jika daftar/detail transaksi gagal dimuat.
    var loadErrorMessage by mutableStateOf("")
        private set
    // Pesan hasil pengiriman data akun dari seller ke buyer.
    var accountMessage by mutableStateOf("")
        private set
    // Detail transaksi yang sedang dibuka; dipakai TransactionDetailScreen untuk status escrow.
    var selectedTransaction by mutableStateOf<TransactionModel?>(null)
        private set
    // Loading aksi detail seperti upload proof, send account, dan complete transaction.
    var isActionLoading by mutableStateOf(false)
        private set

    // Mengunggah bukti pembayaran buyer.
    // Input: token, id transaksi, dan multipart image.
    // Alur: POST /api/transactions/{id}/upload-proof, lalu detail transaksi di-refresh
    // agar status pembayaran terbaru tampil pada layar.
    fun uploadProof(
        token: String,
        transactionId: Int,
        imagePart: MultipartBody.Part
    ) {
        viewModelScope.launch {
            isActionLoading = true
            try {
                // Business logic payment proof: file gambar dikirim sebagai multipart,
                // sedangkan token memastikan hanya pemilik transaksi yang dapat mengunggah bukti.
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
    // Mengambil daftar transaksi milik user terautentikasi.
    // Response TransactionResponse dipetakan menjadi state transactions untuk list screen.
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
    // Seller mengirim data akun digital kepada buyer setelah pembayaran diproses.
    // Input email, password, dan catatan dibungkus repository menjadi SendAccountRequest.
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
                // Business logic escrow: pengiriman akun dilakukan dalam konteks transaksi tertentu,
                // sehingga backend dapat mencatat status bahwa seller telah menyerahkan barang digital.
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
    // Buyer menandai transaksi selesai setelah akun diterima dan valid.
    // Backend kemudian dapat mengubah status escrow menjadi completed.
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
    // Memuat detail satu transaksi untuk layar detail.
    // Detail diperlukan agar UI mengetahui apakah harus menampilkan tombol upload bukti,
    // form pengiriman akun, atau tombol penyelesaian transaksi.
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

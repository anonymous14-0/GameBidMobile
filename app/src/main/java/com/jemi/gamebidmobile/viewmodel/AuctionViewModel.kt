/*
 * File: AuctionViewModel.kt
 * Fungsi: Layer ViewModel dalam arsitektur MVVM. File ini menyimpan state UI, menjalankan coroutine, memanggil Repository, lalu menyediakan hasilnya ke Jetpack Compose.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jemi.gamebidmobile.data.model.AuctionModel
import com.jemi.gamebidmobile.data.model.CategoryModel
import com.jemi.gamebidmobile.data.model.ItemModel
import com.jemi.gamebidmobile.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

// ViewModel pusat untuk fitur auction dan item seller.
// Dipanggil oleh screen Compose seperti AuctionScreen, AuctionDetailScreen,
// SellerAuctionScreen, CreateAuctionScreen, dan CreateItemScreen agar UI
// cukup membaca state tanpa mengetahui detail endpoint Laravel.
class AuctionViewModel : ViewModel() {

    // Repository menjadi satu-satunya objek yang mengakses Retrofit API pada layer ini.
    private val repository = AuctionRepository()

    // State daftar auction yang ditampilkan ke buyer/seller setelah response API berhasil diterima.
    var auctions by mutableStateOf<List<AuctionModel>>(emptyList())
        private set

    // State pesan error untuk kegagalan load data auction, item, atau detail dari jaringan/backend.
    var loadErrorMessage by mutableStateOf("")
        private set

    // Loading global untuk proses pengambilan data dan pembuatan auction.
    var isLoading by mutableStateOf(false)
        private set

    // Pesan hasil submit bid agar UI dapat memberi feedback sukses/gagal kepada buyer.
    var bidMessage by mutableStateOf("")
        private set

    // Loading khusus proses bid supaya tombol bid dapat dinonaktifkan saat request berjalan.
    var isSubmittingBid by mutableStateOf(false)
        private set

    // Daftar item milik seller yang menjadi sumber pilihan saat membuat auction.
    var sellerItems by mutableStateOf<List<ItemModel>>(emptyList())
        private set

    // Pesan hasil pembuatan auction untuk ditampilkan pada form seller.
    var createMessage by mutableStateOf("")
        private set

    // Pesan hasil pembuatan item, termasuk error validasi dari backend.
    var itemMessage by mutableStateOf("")
        private set

    // Daftar kategori dari API yang dipakai pada dropdown pembuatan item.
    var categories by mutableStateOf<List<CategoryModel>>(emptyList())
        private set

    // Detail auction yang sedang dibuka; nullable karena data baru tersedia setelah endpoint detail selesai.
    var selectedAuction by mutableStateOf<AuctionModel?>(null)
        private set

    // Loading khusus pembuatan item agar UI dapat menahan input ganda dan menampilkan progress.
    var isCreatingItem by mutableStateOf(false)
        private set

    // Mengirim bid buyer ke backend.
    // Input: token autentikasi, id auction, dan nominal bid integer yang sudah divalidasi di UI.
    // Alur: ViewModel menyalakan loading, Repository membungkus token menjadi Bearer,
    // Retrofit memanggil POST /api/auctions/{id}/bid, lalu daftar auction di-refresh.
    fun submitBid(
        token: String,
        auctionId: Int,
        bidAmount: Int
    ) {
        viewModelScope.launch {
            isSubmittingBid = true
            bidMessage = ""
            try {
                // Business logic: bid hanya dilanjutkan jika UI menyediakan token dan nominal valid.
                // Backend Laravel tetap menjadi sumber validasi akhir untuk harga minimal, status auction,
                // dan kepemilikan user yang melakukan penawaran.
                repository.submitBid(
                    token,
                    auctionId,
                    bidAmount
                )

                // Setelah bid berhasil, daftar auction dimuat ulang agar harga tertinggi terbaru tampil.
                loadAuctions()
                bidMessage = "Bid berhasil"

            } catch (e: Exception) {
                bidMessage = "Bid gagal: ${e.message}"
            } finally {
                isSubmittingBid = false
            }
        }
    }

    // Mengambil seluruh auction aktif untuk halaman buyer.
    // Output berupa state auctions; error disimpan di loadErrorMessage agar UI dapat menampilkan pesan.
    fun loadAuctions() {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                auctions = repository
                    .getAuctions()
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Mengambil auction milik seller berdasarkan token login.
    // Fungsi ini dipakai dashboard seller agar seller hanya melihat auction yang ia buat.
    fun loadSellerAuctions(token: String) {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                auctions = repository
                    .getSellerAuctions(token)
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Gagal load auction: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Mengambil item milik seller dari API untuk kebutuhan pembuatan auction.
    // Token diperlukan karena endpoint seller bersifat protected.
    fun loadSellerItems(token: String) {
        viewModelScope.launch {
            try {
                sellerItems = repository
                    .getSellerItems(token)
                    .data
            } catch (e: Exception) {
                loadErrorMessage = "Gagal load item: ${e.message}"
            }
        }
    }

    // Membuat auction baru berdasarkan item seller dan rentang waktu lelang.
    // Alur data: form Compose → AuctionViewModel → AuctionRepository → POST /api/seller/auctions.
    fun createAuction(
        token: String,
        itemId: Int,
        startTime: String,
        endTime: String
    ) {
        viewModelScope.launch {
            createMessage = ""
            isLoading = true
            try {
                repository.createAuction(
                    token,
                    itemId,
                    startTime,
                    endTime
                )

                loadSellerAuctions(token)
                createMessage = "Auction berhasil dibuat"

            } catch (e: Exception) {
                createMessage = "Gagal: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Mengambil kategori dari backend agar seller memilih kategori valid sesuai database Laravel.
    fun loadCategories(token: String) {
        viewModelScope.launch {
            categories = repository
                .getCategories(token)
                .data
        }
    }

    // Memuat detail auction berdasarkan auctionId.
    // Dipanggil ketika user membuka AuctionDetailScreen untuk melihat informasi item dan histori bid.
    fun loadAuctionDetail(
        auctionId: Int
    ) {
        viewModelScope.launch {
            isLoading = true
            loadErrorMessage = ""
            try {
                selectedAuction = repository
                    .getAuctionDetail(auctionId)
                    .data

            } catch (e: Exception) {
                loadErrorMessage = "Gagal load detail: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Membuat item baru milik seller dengan multipart form-data.
    // Input berupa RequestBody dan optional image part yang sudah disiapkan UI/FileUtils.
    // Setelah sukses, daftar item seller di-refresh agar item baru langsung tersedia untuk auction.
    fun createItem(
        token: String,
        title: RequestBody,
        categoryId: RequestBody,
        description: RequestBody,
        startingPrice: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                itemMessage = ""
                isCreatingItem = true

                repository.createItem(
                    token,
                    title,
                    categoryId,
                    description,
                    startingPrice,
                    image
                )

                loadSellerItems(token)

                itemMessage = "Item berhasil dibuat"

            } catch (e: Exception) {
                itemMessage = "Gagal: ${e.message}"
            } finally {
                isCreatingItem = false
            }
        }
    }
}

/*
 * File: Screen.kt
 * Fungsi: Layer navigasi aplikasi. File ini membantu menentukan screen/flow yang aktif berdasarkan state aplikasi, role user, dan kebutuhan Navigation Compose.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transaction : Screen("transaction")
    object Profile : Screen("profile")
    object AuctionDetail : Screen("auction_detail/{auctionId}")
    object TransactionDetail :
        Screen("transaction_detail/{transactionId}")
}
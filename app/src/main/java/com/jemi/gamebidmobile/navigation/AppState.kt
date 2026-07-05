/*
 * File: AppState.kt
 * Fungsi: Layer navigasi aplikasi. File ini membantu menentukan screen/flow yang aktif berdasarkan state aplikasi, role user, dan kebutuhan Navigation Compose.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.navigation

sealed class AppState {
    object Splash : AppState()
    object Login : AppState()

    object BuyerDashboard : AppState()
    object SellerDashboard : AppState()
}
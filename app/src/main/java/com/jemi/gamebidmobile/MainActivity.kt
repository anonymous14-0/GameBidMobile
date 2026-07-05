/*
 * File: MainActivity.kt
 * Fungsi: Entry point aplikasi Android. File ini menginisialisasi tema Compose, splash flow, pengecekan token, serta navigasi awal berdasarkan role dari backend Laravel.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.navigation.AppState
import com.jemi.gamebidmobile.ui.auth.LoginScreen
import com.jemi.gamebidmobile.ui.dashboard.BuyerDashboardScreen
import com.jemi.gamebidmobile.ui.dashboard.SellerDashboardScreen
import com.jemi.gamebidmobile.ui.splash.SplashScreen
import com.jemi.gamebidmobile.ui.theme.GameBidMobileTheme

// MainActivity adalah entry point aplikasi GameBid Mobile.
// Di sinilah Compose content dipasang, tema Material 3 diterapkan,
// sesi login dibaca dari TokenManager, dan navigasi awal dipilih berdasarkan role.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameBidMobileTheme {

                // Context dibutuhkan untuk membuat TokenManager yang mengakses SharedPreferences.
                val context = LocalContext.current
                // remember memastikan TokenManager tidak dibuat ulang setiap recomposition.
                val tokenManager = remember {
                    TokenManager(context)
                }

                // State aplikasi paling luar: Splash, Login, BuyerDashboard, atau SellerDashboard.
                // State ini sederhana karena root flow tidak memakai NavController.
                var appState by remember {
                    mutableStateOf<AppState>(AppState.Splash)
                }

                // Efek hanya berjalan sekali saat composable pertama kali masuk composition.
                // Setelah splash delay, aplikasi membaca token dan role untuk menentukan tujuan awal.
                LaunchedEffect(Unit) {
                    delay(2000)

                    val token = tokenManager.getToken()
                    val role = tokenManager.getRole()

                    // Role-based navigation:
                    // jika token masih ada, user langsung masuk dashboard sesuai role dari backend;
                    // jika tidak ada token, user diarahkan ke LoginScreen.
                    appState =
                        if (token != null) {
                            when (role) {
                                "penjual" -> AppState.SellerDashboard
                                else -> AppState.BuyerDashboard
                            }
                        } else {
                            AppState.Login
                        }
                }

                when (appState) {
                    AppState.Splash -> SplashScreen()

                    AppState.Login -> LoginScreen(
                        onLoginSuccess = {
                            // Setelah LoginScreen menyimpan token dan role,
                            // MainActivity membaca role terbaru untuk memilih dashboard.
                            val role = tokenManager.getRole()

                            appState = when (role) {
                                "penjual" -> AppState.SellerDashboard
                                else -> AppState.BuyerDashboard
                            }
                        }
                    )

                    AppState.BuyerDashboard -> BuyerDashboardScreen(
                        onLogout = {
                            appState = AppState.Login
                        }
                    )
                    AppState.SellerDashboard -> SellerDashboardScreen(
                        onLogout = {
                            appState = AppState.Login
                        }
                    )
                }
            }
        }
    }
}

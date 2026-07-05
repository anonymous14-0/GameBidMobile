/*
 * File: AuthViewModel.kt
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
import com.jemi.gamebidmobile.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

// ViewModel autentikasi untuk login user GameBid Mobile.
// Dipanggil LoginScreen ketika user mengisi credential; hasilnya berupa token,
// role, loading, dan pesan validasi yang kemudian disimpan TokenManager di layer UI.
class AuthViewModel : ViewModel() {

    // Repository memisahkan logic UI dari pemanggilan endpoint POST /api/login.
    private val repository = AuthRepository()

    // Pesan autentikasi dari backend atau pesan error yang dipetakan untuk pengguna.
    var loginMessage by mutableStateOf("")
        private set

    // State keberhasilan login yang diamati LoginScreen untuk memicu navigasi berbasis role.
    var loginSuccess by mutableStateOf(false)
        private set

    // Token Bearer hasil login; token ini diteruskan ke TokenManager untuk disimpan di SharedPreferences.
    var token by mutableStateOf("")
        private set

    // Role user dari Laravel (misalnya pembeli/penjual) untuk menentukan dashboard tujuan.
    var role by mutableStateOf("")
        private set

    // Loading login agar UI dapat menampilkan progress dan mencegah submit berulang.
    var isLoading by mutableStateOf(false)
        private set


    // Melakukan proses login.
    // Input: email dan password dari form.
    // Output: state token, role, loginSuccess, dan loginMessage.
    // Alur: UI → AuthViewModel → AuthRepository → Retrofit → Laravel /api/login.
    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true

                // Credentials dikirim sebagai LoginRequest melalui repository.
                val response = repository.login(email, password)

                // Business logic role-based access:
                // admin sengaja ditolak dari aplikasi mobile karena role admin hanya untuk dashboard web.
                if (response.user.role == "admin") {
                    loginSuccess = false
                    loginMessage = "Admin hanya bisa login via web"
                    return@launch
                }

                loginMessage = response.message
                loginSuccess = response.status
                // Token dan role disimpan di state sementara; LoginScreen kemudian menyimpannya
                // ke SharedPreferences agar sesi tetap aktif setelah aplikasi dibuka ulang.
                token = response.token
                role = response.user.role

            } catch (e: HttpException) {
                loginSuccess = false

                // Mapping HTTP error Laravel menjadi pesan yang lebih mudah dipahami user.
                loginMessage = when (e.code()) {
                    401 -> "Email atau password salah"
                    422 -> "Data login tidak valid"
                    500 -> "Server error"
                    else -> "Login gagal"
                }

            } catch (e: Exception) {
                loginSuccess = false
                loginMessage = "Tidak dapat terhubung ke server"
            } finally {
                isLoading = false
            }
        }
    }
    // Membersihkan state login ketika screen ditinggalkan atau setelah navigasi selesai,
    // sehingga pesan lama tidak muncul lagi saat user kembali ke LoginScreen.
    fun resetLoginState() {
        loginSuccess = false
        loginMessage = ""
        token = ""
        role = ""
    }
}

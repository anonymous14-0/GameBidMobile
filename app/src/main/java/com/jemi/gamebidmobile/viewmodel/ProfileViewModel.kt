/*
 * File: ProfileViewModel.kt
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
import com.jemi.gamebidmobile.data.model.UserModel
import com.jemi.gamebidmobile.data.repository.ProfileRepository
import kotlinx.coroutines.launch

// ViewModel profil user.
// Dipanggil ProfileScreen untuk menampilkan data user yang sedang login tanpa membuat UI
// mengetahui detail Authorization header atau endpoint /api/profile.
class ProfileViewModel : ViewModel() {

    // Repository profil mengakses ApiService dan menambahkan Bearer token.
    private val repository = ProfileRepository()

    // State user hasil response backend; nullable karena data belum tersedia sebelum loadProfile selesai.
    var user by mutableStateOf<UserModel?>(null)
        private set

    // Pesan error untuk kegagalan jaringan/backend saat memuat profil.
    var errorMessage by mutableStateOf("")
        private set

    // Memuat profil user berdasarkan token login.
    // Alur: ProfileScreen → ProfileViewModel → ProfileRepository → GET /api/profile.
    fun loadProfile(token: String) {
        viewModelScope.launch {
            try {
                user =
                    repository
                        .getProfile(token)
                        .data
            } catch (e: Exception) {
                errorMessage =
                    "Gagal load profile: ${e.message}"
            }
        }
    }
}

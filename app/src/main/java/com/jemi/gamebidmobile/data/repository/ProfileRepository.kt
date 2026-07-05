/*
 * File: ProfileRepository.kt
 * Fungsi: Layer Repository yang mengabstraksi pemanggilan Retrofit API. Repository dipanggil ViewModel agar UI tidak berkomunikasi langsung dengan backend Laravel.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.data.repository

import com.jemi.gamebidmobile.data.remote.RetrofitClient

// Repository ini berperan sebagai adapter antara ViewModel dan ApiService.
// Semua token dari UI/ViewModel diubah menjadi header Bearer di sini agar format autentikasi konsisten.
class ProfileRepository {

    suspend fun getProfile(token: String) =
        RetrofitClient.api.getProfile(
            "Bearer $token"
        )
}
/*
 * File: TokenManager.kt
 * Fungsi: Layer penyimpanan lokal. File ini berhubungan dengan SharedPreferences untuk menyimpan token autentikasi dan role yang diterima dari backend Laravel.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.data.local

import android.content.Context

// TokenManager bertanggung jawab menyimpan sesi login secara lokal.
// Data token dan role berasal dari response login Laravel, lalu dipakai kembali
// untuk Authorization header dan penentuan dashboard saat aplikasi dibuka ulang.
class TokenManager(context: Context) {

    // SharedPreferences dipakai karena kebutuhan penyimpanan sederhana:
    // token, role, dan status sesi tanpa database lokal.
    private val prefs =
        context.getSharedPreferences(
            "gamebid_prefs",
            Context.MODE_PRIVATE
        )

    // Menyimpan token autentikasi dari backend.
    // Token ini akan dikirim ke API protected dengan format header "Bearer <token>".
    fun saveToken(token: String) {
        prefs.edit()
            .putString("token", token)
            .apply()
    }

    // Mengambil token untuk pengecekan sesi di MainActivity dan request API di screen/ViewModel.
    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    // Menyimpan role user (penjual/pembeli) untuk role-based navigation.
    fun saveRole(role: String) {
        prefs.edit()
            .putString("role", role)
            .apply()
    }

    // Mengambil role agar aplikasi dapat menentukan dashboard yang sesuai.
    fun getRole(): String? {
        return prefs.getString("role", null)
    }

    // Menghapus seluruh data preferensi GameBid, digunakan saat perlu reset sesi penuh.
    fun clearAll() {
        prefs.edit()
            .clear()
            .apply()
    }

    // Logout ringan: menghapus token dan role sehingga user diarahkan kembali ke LoginScreen.
    fun clearToken() {
        prefs.edit()
            .remove("token")
            .remove("role")
            .apply()
    }
}

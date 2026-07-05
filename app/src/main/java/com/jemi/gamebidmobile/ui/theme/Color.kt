/*
 * File: Color.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.theme

import androidx.compose.ui.graphics.Color

val PrimaryPurple = Color(0xFF7C4DFF)
val SecondaryPurple = Color(0xFFB388FF)
val DarkPurple = Color(0xFF512DA8)
val LightBackground = Color(0xFFF7F3FF)
val SurfaceWhite = Color(0xFFFFFFFF)
val Success = Color(0xFF4CAF50)
val Warning = Color(0xFFFF9800)
val Error = Color(0xFFD32F2F)

val PurpleContainer = Color(0xFFEDE7FF)
val PurpleSurfaceVariant = Color(0xFFF0E8FF)
val OutlinePurple = Color(0xFFD6C6FF)
val PremiumText = Color(0xFF1F1235)
val MutedText = Color(0xFF6F6384)
val DarkBackground = Color(0xFF151022)
val DarkSurface = Color(0xFF211934)
val DarkSurfaceVariant = Color(0xFF33284A)

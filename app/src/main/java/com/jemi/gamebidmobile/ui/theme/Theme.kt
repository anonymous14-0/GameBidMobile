/*
 * File: Theme.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SecondaryPurple,
    onPrimary = DarkPurple,
    primaryContainer = DarkPurple,
    onPrimaryContainer = PurpleContainer,
    secondary = PrimaryPurple,
    onSecondary = SurfaceWhite,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = PurpleContainer,
    tertiary = Success,
    onTertiary = SurfaceWhite,
    error = Error,
    onError = SurfaceWhite,
    background = DarkBackground,
    onBackground = PurpleContainer,
    surface = DarkSurface,
    onSurface = PurpleContainer,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OutlinePurple,
    outline = OutlinePurple
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = SurfaceWhite,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = DarkPurple,
    secondary = SecondaryPurple,
    onSecondary = SurfaceWhite,
    secondaryContainer = PurpleSurfaceVariant,
    onSecondaryContainer = DarkPurple,
    tertiary = Success,
    onTertiary = SurfaceWhite,
    tertiaryContainer = Success.copy(alpha = 0.14f),
    onTertiaryContainer = Success,
    error = Error,
    onError = SurfaceWhite,
    errorContainer = Error.copy(alpha = 0.12f),
    onErrorContainer = Error,
    background = LightBackground,
    onBackground = PremiumText,
    surface = SurfaceWhite,
    onSurface = PremiumText,
    surfaceVariant = PurpleSurfaceVariant,
    onSurfaceVariant = MutedText,
    outline = OutlinePurple,
    inversePrimary = SecondaryPurple
)

// Composable ini membangun bagian UI GameBidMobileTheme.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun GameBidMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

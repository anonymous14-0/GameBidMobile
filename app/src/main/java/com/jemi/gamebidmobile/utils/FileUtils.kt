/*
 * File: FileUtils.kt
 * Fungsi: Bagian dari aplikasi GameBid Mobile yang mendukung arsitektur Kotlin, Jetpack Compose, MVVM, dan komunikasi Laravel REST API.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(
    context: Context,
    uri: Uri
): File {
    val inputStream =
        context.contentResolver.openInputStream(uri)

    val file = File(
        context.cacheDir,
        "upload_image.jpg"
    )

    val outputStream = FileOutputStream(file)

    inputStream?.copyTo(outputStream)

    inputStream?.close()
    outputStream.close()

    return file
}
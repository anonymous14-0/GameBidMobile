/*
 * File: ProfileScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.ProfileViewModel
import com.jemi.gamebidmobile.ui.components.ConfirmActionDialog

// Composable ini membangun bagian UI ProfileScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadProfile(token)
        }
    }

    val user = viewModel.user
    val role = user?.role ?: tokenManager.getRole() ?: "Unknown"
    val username = user?.name ?: "Loading..."
    val email = user?.email ?: "-"
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }
    // State UI: SnackbarHostState menampung antrean pesan validasi/sukses dari ViewModel agar feedback tampil konsisten.
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorMessage) {
        if (viewModel.errorMessage.isNotEmpty()) snackbarHostState.showSnackbar(viewModel.errorMessage)
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.firstOrNull()?.uppercase() ?: "U",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = username,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = role.replaceFirstChar {
                it.uppercase()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Email : $email")
                Text("Role : $role")
                Text("Version : 1.0")
                Text("Status : Active")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                showLogoutDialog = true
            }
        ) {
            Text("Logout")
        }
        if (showLogoutDialog) {
            ConfirmActionDialog(
                title = "Logout",
                message = "Anda yakin ingin keluar dari akun GameBid?",
                confirmText = "Logout",
                isDestructive = true,
                onConfirm = {
                    tokenManager.clearToken()
                    onLogout()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }
    }
    }
}

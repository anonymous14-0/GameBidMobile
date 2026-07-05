/*
 * File: AuctionDetailScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import androidx.compose.ui.platform.LocalContext
import com.jemi.gamebidmobile.data.local.TokenManager
import kotlinx.coroutines.delay
import com.jemi.gamebidmobile.ui.components.calculateRemainingTime
import com.jemi.gamebidmobile.ui.components.formatRupiah
import com.jemi.gamebidmobile.ui.components.StatusBadge
import com.jemi.gamebidmobile.ui.components.ErrorState
import com.jemi.gamebidmobile.ui.components.ConfirmActionDialog
import com.jemi.gamebidmobile.ui.components.LoadingButtonContent
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Composable ini membangun bagian UI AuctionDetailScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun AuctionDetailScreen(
    auctionId: Int,
    viewModel: AuctionViewModel = viewModel()
) {
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var bidAmount by remember { mutableStateOf("") }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var remainingTime by remember { mutableStateOf("") }
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    var showBidDialog by remember { mutableStateOf(false) }

    // State UI: SnackbarHostState menampung antrean pesan validasi/sukses dari ViewModel agar feedback tampil konsisten.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    // State UI: nilai ini disimpan dengan remember/mutableStateOf agar perubahan input pengguna memicu recomposition tanpa menyentuh layer data.
    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        viewModel.loadAuctionDetail(auctionId)
    }

    LaunchedEffect(viewModel.bidMessage) {
        if (viewModel.bidMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                viewModel.bidMessage
            )
        }
    }

    val auction = viewModel.selectedAuction

    if (auction == null) {
        if (viewModel.loadErrorMessage.isNotEmpty()) {
            ErrorState(
                title = "Detail auction gagal dimuat",
                subtitle = "Kami belum bisa menampilkan detail auction ini. Coba muat ulang halaman.",
                onActionClick = {
                    viewModel.loadAuctionDetail(auctionId)
                }
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    LaunchedEffect(auction.end_time) {
        while (true) {
            remainingTime =
                calculateRemainingTime(
                    auction.end_time
                )
            delay(1000)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // FIX
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            auction.item.image?.let { imagePath ->
                AsyncImage(
                    model = "http://192.168.1.107:8000/storage/$imagePath",
                    contentDescription = auction.item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Text(
                text = auction.item.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Harga Saat Ini",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = formatRupiah(auction.current_price),
                style = MaterialTheme.typography.headlineSmall
            )

            StatusBadge(auction.status)

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "⏱ $remainingTime",
                    modifier = Modifier.padding(12.dp)
                )
            }

            OutlinedTextField(
                value = bidAmount,
                onValueChange = {
                    bidAmount = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Masukkan Bid")
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !viewModel.isSubmittingBid,
                onClick = {
                    when {
                        token == null -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Sesi login tidak ditemukan"
                                )
                            }
                        }

                        bidAmount.toIntOrNull() == null -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Masukkan nominal bid yang valid"
                                )
                            }
                        }

                        else -> {
                            showBidDialog = true
                        }
                    }
                }
            ) {
                LoadingButtonContent(
                    "Bid Sekarang",
                    viewModel.isSubmittingBid
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showBidDialog) {
        ConfirmActionDialog(
            title = "Konfirmasi Bid",
            message =
                "Kirim bid sebesar ${
                    formatRupiah(
                        bidAmount.toIntOrNull() ?: 0
                    )
                } untuk auction ini?",
            confirmText = "Kirim Bid",
            onConfirm = {
                showBidDialog = false

                val amount =
                    bidAmount.toIntOrNull()

                if (token != null && amount != null) {
                    viewModel.submitBid(
                        token,
                        auctionId,
                        amount
                    )
                }
            },
            onDismiss = {
                showBidDialog = false
            }
        )
    }
}

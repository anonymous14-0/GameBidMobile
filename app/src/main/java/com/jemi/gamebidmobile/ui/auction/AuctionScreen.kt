/*
 * File: AuctionScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jemi.gamebidmobile.ui.components.EmptyState
import com.jemi.gamebidmobile.ui.components.ErrorState
import com.jemi.gamebidmobile.ui.components.StatusBadge
import com.jemi.gamebidmobile.ui.components.calculateRemainingTime
import com.jemi.gamebidmobile.ui.components.formatRupiah
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import kotlinx.coroutines.delay

// Composable ini membangun bagian UI AuctionScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun AuctionScreen(
    navController: NavController,
    viewModel: AuctionViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadAuctions()
    }

    when {
        viewModel.loadErrorMessage.isNotEmpty() -> {
            ErrorState(
                subtitle = "Auction belum bisa ditampilkan. Coba lagi.",
                onActionClick = { viewModel.loadAuctions() }
            )
        }

        viewModel.auctions.isEmpty() && !viewModel.isLoading -> {
            EmptyState(
                icon = Icons.Outlined.Gavel,
                title = "Belum ada auction",
                subtitle = "Auction aktif akan muncul di sini.",
                actionLabel = "Refresh",
                onActionClick = { viewModel.loadAuctions() }
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Column {
                        Text(
                            text = "Halo, Jemi 👋",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Temukan item terbaik hari ini",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                items(viewModel.auctions) { auction ->

                    var remainingTime by remember {
                        mutableStateOf("")
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

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "auction_detail/${auction.id}"
                                )
                            },
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {

                        Column {

                            Box {
                                auction.item.image?.let { imagePath ->
                                    AsyncImage(
                                        model = "http://192.168.1.107:8000/storage/$imagePath",
                                        contentDescription = auction.item.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(12.dp)
                                ) {
                                    StatusBadge(auction.status)
                                }
                            }

                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {

                                Text(
                                    text = auction.item.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Text(
                                    text = "Current Bid",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = formatRupiah(
                                        auction.current_price
                                    ),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                                ) {
                                    Text(
                                        text = "⏱ $remainingTime",
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Lihat Detail",
                                        color = MaterialTheme
                                            .colorScheme
                                            .primary,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Icon(
                                        imageVector =
                                            Icons.Default.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = MaterialTheme
                                            .colorScheme
                                            .primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

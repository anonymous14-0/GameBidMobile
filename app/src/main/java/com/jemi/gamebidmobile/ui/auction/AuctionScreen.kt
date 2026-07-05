package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun AuctionScreen(
    navController: NavController,
    viewModel: AuctionViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadAuctions()
    }

    when {
        viewModel.loadErrorMessage.isNotEmpty() -> ErrorState(
            subtitle = "Auction belum bisa ditampilkan. Periksa koneksi internet lalu coba lagi.",
            onActionClick = { viewModel.loadAuctions() }
        )

        viewModel.auctions.isEmpty() && !viewModel.isLoading -> EmptyState(
            icon = Icons.Outlined.Gavel,
            title = "Belum ada auction",
            subtitle = "Auction aktif akan muncul di sini saat seller mulai melelang item.",
            actionLabel = "Refresh",
            onActionClick = { viewModel.loadAuctions() }
        )

        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.auctions) { auction ->
                var remainingTime by remember { mutableStateOf("") }

                LaunchedEffect(auction.end_time) {
                    while (true) {
                        remainingTime = calculateRemainingTime(auction.end_time)
                        delay(1000)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("auction_detail/${auction.id}")
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column {
                        auction.item.image?.let { imagePath ->
                            AsyncImage(
                                model = "http://192.168.1.107:8000/storage/$imagePath",
                                contentDescription = auction.item.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 20.dp
                                        )
                                    )
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = auction.item.title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Harga Saat Ini",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = formatRupiah(auction.current_price),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            StatusBadge(auction.status)

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "⏱ $remainingTime",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

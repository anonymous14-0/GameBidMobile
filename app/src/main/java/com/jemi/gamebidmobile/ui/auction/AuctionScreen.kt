package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import com.jemi.gamebidmobile.ui.components.calculateRemainingTime
import com.jemi.gamebidmobile.ui.components.formatRupiah
import com.jemi.gamebidmobile.ui.components.StatusBadge

@Composable
fun AuctionScreen(
    navController: NavController,
    viewModel: AuctionViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadAuctions()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
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
                            text = formatRupiah(
                                auction.current_price
                            ),
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

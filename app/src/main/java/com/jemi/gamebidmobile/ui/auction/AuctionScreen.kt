package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel

@Composable
fun AuctionScreen(
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = auction.item.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Harga Saat Ini",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Rp ${auction.current_price}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Status: ${auction.status}"
                    )
                }
            }
        }
    }
}
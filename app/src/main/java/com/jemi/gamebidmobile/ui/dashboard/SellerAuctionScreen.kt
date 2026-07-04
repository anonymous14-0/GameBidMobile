package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel

@Composable
fun SellerAuctionScreen(
    navController: NavController,
    viewModel: AuctionViewModel = viewModel()
) {
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadSellerAuctions(token)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Button(
            onClick = {
                navController.navigate("create_auction")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Create Auction")
        }
        Button(
            onClick = {
                navController.navigate("create_item")
            }
        ) {
            Text("Create Item")
        }
        if (viewModel.auctions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Belum ada auction",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
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

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                "Harga Saat Ini: Rp ${auction.current_price}"
                            )
                            Text(
                                "Status: ${auction.status}"
                            )
                        }
                    }
                }
            }
        }
    }
}
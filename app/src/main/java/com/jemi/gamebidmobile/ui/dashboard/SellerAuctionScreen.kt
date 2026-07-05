package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import com.jemi.gamebidmobile.ui.components.StatusBadge
import com.jemi.gamebidmobile.ui.components.formatRupiah
import androidx.compose.ui.Alignment

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

        Text(
            text = "Seller Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("create_item")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Create Item")
            }

            Button(
                onClick = {
                    navController.navigate("create_auction")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Create Auction")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.auctions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📦",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Belum ada auction",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Buat item atau auction baru")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(viewModel.auctions) { auction ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                        .height(180.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = auction.item.title,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Text(
                                    text = formatRupiah(
                                        auction.current_price
                                    ),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                StatusBadge(
                                    auction.status
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
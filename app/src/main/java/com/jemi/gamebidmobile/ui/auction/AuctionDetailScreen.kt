package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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

@Composable
fun AuctionDetailScreen(
    auctionId: Int,
    viewModel: AuctionViewModel = viewModel()
) {
    var bidAmount by remember {
        mutableStateOf("")
    }

    var remainingTime by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        viewModel.loadAuctionDetail(auctionId)
    }

    val auction = viewModel.selectedAuction

    if (auction == null) {
        if (viewModel.loadErrorMessage.isNotEmpty()) {
            ErrorState(
                title = "Detail auction gagal dimuat",
                subtitle = "Kami belum bisa menampilkan detail auction ini. Coba muat ulang halaman.",
                onActionClick = { viewModel.loadAuctionDetail(auctionId) }
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = auction.item.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Harga Saat Ini",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = formatRupiah(auction.current_price),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (
                    token != null &&
                    bidAmount.isNotEmpty()
                ) {
                    viewModel.submitBid(
                        token,
                        auctionId,
                        bidAmount.toInt()
                    )
                }
            }
        ) {
            Text("Bid Sekarang")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(viewModel.bidMessage)
    }
}
package com.jemi.gamebidmobile.ui.auction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jemi.gamebidmobile.viewmodel.AuctionViewModel
import androidx.compose.ui.platform.LocalContext
import com.jemi.gamebidmobile.data.local.TokenManager
@Composable
fun AuctionDetailScreen(
    auctionId: Int,
    viewModel: AuctionViewModel = viewModel()
) {
    var bidAmount by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Detail Auction ID: $auctionId",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bidAmount,
            onValueChange = {
                bidAmount = it
            },
            label = {
                Text("Masukkan Bid")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (token != null) {
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
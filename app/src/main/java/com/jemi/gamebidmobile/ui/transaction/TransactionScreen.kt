package com.jemi.gamebidmobile.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.viewmodel.TransactionViewModel
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
){
    val context = LocalContext.current

    val tokenManager = remember {
        TokenManager(context)
    }

    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.loadTransactions(token)
        }
    }

    if (viewModel.transactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Belum ada transaksi",
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.transactions) { transaction ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                "transaction_detail/${transaction.id}"
                            )
                        },
                    elevation = CardDefaults.cardElevation(6.dp)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Transaksi #${transaction.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Auction ID: ${transaction.auction_id}")

                        Text("Total: Rp ${transaction.amount}")

                        Text("Status: ${transaction.status}")
                    }
                }
            }
        }
    }
}
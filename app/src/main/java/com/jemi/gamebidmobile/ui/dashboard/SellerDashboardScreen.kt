package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jemi.gamebidmobile.ui.auction.CreateAuctionScreen
import com.jemi.gamebidmobile.ui.profile.ProfileScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionScreen
import com.jemi.gamebidmobile.ui.item.CreateItemScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionDetailScreen
@Composable
fun SellerDashboardScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("seller_auction")
                    },
                    icon = {},
                    label = { Text("Auction") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("seller_sales")
                    },
                    icon = {},
                    label = { Text("Penjualan") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("seller_profile")
                    },
                    icon = {},
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier.padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "seller_auction"
            ) {

                composable("seller_auction") {
                    SellerAuctionScreen(navController)
                }

                composable("seller_sales") {
                    TransactionScreen(navController)
                }

                composable("seller_profile") {
                    ProfileScreen()
                }

                composable("create_auction") {
                    CreateAuctionScreen()
                }
                composable("create_item") {
                    CreateItemScreen()
                }
                composable("transaction_detail/{transactionId}") { backStackEntry ->

                    val transactionId =
                        backStackEntry.arguments
                            ?.getString("transactionId")
                            ?.toIntOrNull() ?: 0

                    TransactionDetailScreen(
                        transactionId = transactionId
                    )
                }
            }
        }
    }
}
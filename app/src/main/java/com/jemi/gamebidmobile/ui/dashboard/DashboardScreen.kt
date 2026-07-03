package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.jemi.gamebidmobile.navigation.Screen
import com.jemi.gamebidmobile.ui.auction.AuctionScreen
import com.jemi.gamebidmobile.ui.profile.ProfileScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionScreen
import com.jemi.gamebidmobile.ui.auction.AuctionDetailScreen
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.jemi.gamebidmobile.ui.transaction.TransactionDetailScreen
@Composable
fun DashboardScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    icon = {},
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Transaction.route)
                    },
                    icon = {},
                    label = { Text("Transaksi") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Profile.route)
                    },
                    icon = {},
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ){

            composable(Screen.Home.route) {
                AuctionScreen(navController)
            }

            composable(Screen.Transaction.route) {
                TransactionScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = "auction_detail/{auctionId}"
            ) { backStackEntry ->

                val auctionId =
                    backStackEntry.arguments
                        ?.getString("auctionId")
                        ?.toIntOrNull() ?: 0

                AuctionDetailScreen(auctionId)
            }
            composable(
                route = Screen.TransactionDetail.route
            ) { backStackEntry ->

                val transactionId =
                    backStackEntry.arguments
                        ?.getString("transactionId")
                        ?.toIntOrNull() ?: 0

                TransactionDetailScreen(transactionId)
            }
        }
    }
}
package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jemi.gamebidmobile.ui.auction.CreateAuctionScreen
import com.jemi.gamebidmobile.ui.item.CreateItemScreen
import com.jemi.gamebidmobile.ui.profile.ProfileScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionDetailScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionScreen

@Composable
fun SellerDashboardScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ) {

                NavigationBarItem(
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate("seller_auction") {
                            launchSingleTop = true
                            popUpTo("seller_auction")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = "Auction"
                        )
                    },
                    label = {
                        Text("Auction")
                    }
                )

                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate("seller_sales") {
                            launchSingleTop = true
                            popUpTo("seller_auction")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Penjualan"
                        )
                    },
                    label = {
                        Text("Penjualan")
                    }
                )

                NavigationBarItem(
                    selected = selectedIndex == 2,
                    onClick = {
                        selectedIndex = 2
                        navController.navigate("seller_profile") {
                            launchSingleTop = true
                            popUpTo("seller_auction")
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
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
                    ProfileScreen(onLogout)
                }

                composable("create_auction") {
                    CreateAuctionScreen()
                }

                composable("create_item") {
                    CreateItemScreen()
                }

                composable(
                    "transaction_detail/{transactionId}"
                ) { backStackEntry ->

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
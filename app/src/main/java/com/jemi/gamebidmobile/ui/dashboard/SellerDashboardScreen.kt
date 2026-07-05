/*
 * File: SellerDashboardScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jemi.gamebidmobile.ui.auction.CreateAuctionScreen
import com.jemi.gamebidmobile.ui.item.CreateItemScreen
import com.jemi.gamebidmobile.ui.profile.ProfileScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionDetailScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionScreen

// Composable ini membangun bagian UI SellerDashboardScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun SellerDashboardScreen(
    onLogout: () -> Unit
) {
    // State UI: NavController diingat agar navigasi antar tab/screen tetap stabil selama recomposition Compose.
    val navController = rememberNavController()

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
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
                    },
                    colors = premiumNavigationBarItemColors()
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
                    },
                    colors = premiumNavigationBarItemColors()
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
                    },
                    colors = premiumNavigationBarItemColors()
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

// Composable ini membangun bagian UI premiumNavigationBarItemColors.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
private fun premiumNavigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)

/*
 * File: BuyerDashboardScreen.kt
 * Fungsi: Layer UI Jetpack Compose. File ini membangun tampilan, membaca state dari ViewModel, dan mengirim event pengguna ke alur UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 * Peran arsitektur: menjaga pemisahan tanggung jawab antar layer sehingga kode UI, state, penyimpanan lokal, dan komunikasi API tetap mudah dijelaskan saat skripsi/presentasi.
 * Keterkaitan API: bila file ini tidak memanggil API secara langsung, data tetap mengalir melalui chain UI → ViewModel → Repository → Retrofit API → Laravel Backend.
 */

package com.jemi.gamebidmobile.ui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.jemi.gamebidmobile.navigation.Screen
import com.jemi.gamebidmobile.ui.auction.AuctionDetailScreen
import com.jemi.gamebidmobile.ui.auction.AuctionScreen
import com.jemi.gamebidmobile.ui.profile.ProfileScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionDetailScreen
import com.jemi.gamebidmobile.ui.transaction.TransactionScreen

// Composable ini membangun bagian UI BuyerDashboardScreen.
// Dipanggil oleh flow navigasi/screen terkait; event pengguna diteruskan ke ViewModel atau callback tanpa mengubah logic bisnis di UI.
@Composable
fun BuyerDashboardScreen(
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
                        navController.navigate(Screen.Home.route) {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text("Home")
                    },
                    colors = premiumNavigationBarItemColors()
                )

                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(
                            Screen.Transaction.route
                        ) {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector =
                                Icons.Default.ShoppingCart,
                            contentDescription = "Transaksi"
                        )
                    },
                    label = {
                        Text("Transaksi")
                    },
                    colors = premiumNavigationBarItemColors()
                )

                NavigationBarItem(
                    selected = selectedIndex == 2,
                    onClick = {
                        selectedIndex = 2
                        navController.navigate(Screen.Profile.route) {
                            launchSingleTop = true
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

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Home.route) {
                AuctionScreen(navController)
            }

            composable(Screen.Transaction.route) {
                TransactionScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onLogout)
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

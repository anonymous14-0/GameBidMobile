package com.jemi.gamebidmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import com.jemi.gamebidmobile.data.local.TokenManager
import com.jemi.gamebidmobile.navigation.AppState
import com.jemi.gamebidmobile.ui.auth.LoginScreen
import com.jemi.gamebidmobile.ui.dashboard.BuyerDashboardScreen
import com.jemi.gamebidmobile.ui.dashboard.SellerDashboardScreen
import com.jemi.gamebidmobile.ui.splash.SplashScreen
import com.jemi.gamebidmobile.ui.theme.GameBidMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameBidMobileTheme {

                val context = LocalContext.current
                val tokenManager = remember {
                    TokenManager(context)
                }

                var appState by remember {
                    mutableStateOf<AppState>(AppState.Splash)
                }

                LaunchedEffect(Unit) {
                    delay(2000)

                    val token = tokenManager.getToken()
                    val role = tokenManager.getRole()

                    appState =
                        if (token != null) {
                            when (role) {
                                "penjual" -> AppState.SellerDashboard
                                else -> AppState.BuyerDashboard
                            }
                        } else {
                            AppState.Login
                        }
                }

                when (appState) {
                    AppState.Splash -> SplashScreen()

                    AppState.Login -> LoginScreen(
                        onLoginSuccess = {
                            val role = tokenManager.getRole()

                            appState = when (role) {
                                "penjual" -> AppState.SellerDashboard
                                else -> AppState.BuyerDashboard
                            }
                        }
                    )

                    AppState.BuyerDashboard -> BuyerDashboardScreen(
                        onLogout = {
                            appState = AppState.Login
                        }
                    )
                    AppState.SellerDashboard -> SellerDashboardScreen(
                        onLogout = {
                            appState = AppState.Login
                        }
                    )
                }
            }
        }
    }
}
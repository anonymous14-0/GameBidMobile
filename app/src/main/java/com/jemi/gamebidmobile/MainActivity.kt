package com.jemi.gamebidmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jemi.gamebidmobile.ui.auction.AuctionScreen
import com.jemi.gamebidmobile.ui.theme.GameBidMobileTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GameBidMobileTheme {
                AuctionScreen()
            }
        }
    }
}
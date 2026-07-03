package com.jemi.gamebidmobile.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transaction : Screen("transaction")
    object Profile : Screen("profile")
    object AuctionDetail : Screen("auction_detail/{auctionId}")
    object TransactionDetail :
        Screen("transaction_detail/{transactionId}")
}
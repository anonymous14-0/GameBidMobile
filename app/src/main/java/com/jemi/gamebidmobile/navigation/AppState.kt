package com.jemi.gamebidmobile.navigation

sealed class AppState {
    object Splash : AppState()
    object Login : AppState()
    object Dashboard : AppState()
}
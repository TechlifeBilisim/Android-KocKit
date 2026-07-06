package com.techlife.kockit.core.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateToMainClearingBackStack() {
    navigate(Screen.Main.route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToLoginClearingBackStack() {
    navigate(Screen.Login.route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

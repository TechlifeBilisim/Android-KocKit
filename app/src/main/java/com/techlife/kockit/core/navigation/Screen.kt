package com.techlife.kockit.core.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object GoalSetup : Screen("goal_setup")
    data object Home : Screen("home")
}

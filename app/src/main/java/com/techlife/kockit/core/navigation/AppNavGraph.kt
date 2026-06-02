package com.techlife.kockit.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techlife.kockit.feature.auth.forgotpassword.ForgotPasswordScreen
import com.techlife.kockit.feature.auth.login.LoginScreen
import com.techlife.kockit.feature.auth.login.LoginViewModel
import com.techlife.kockit.feature.auth.register.RegisterScreen
import com.techlife.kockit.feature.auth.register.RegisterViewModel
import com.techlife.kockit.feature.goalsetup.GoalSetupScreen
import com.techlife.kockit.feature.goalsetup.GoalSetupViewModel
import com.techlife.kockit.feature.home.HomeScreen
import com.techlife.kockit.feature.splash.SplashScreen
import com.techlife.kockit.feature.splash.SplashViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        // Back stack kalsın (geri ikonu çalışsın).
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToGoalSetup = {
                    navController.navigate(Screen.GoalSetup.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onShowMessage = ::showToast
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToGoalSetup = {
                    navController.navigate(Screen.GoalSetup.route)
                },
                onNavigateBack = { navController.popBackStack() },
                onShowMessage = ::showToast
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onShowMessage = ::showToast
            )
        }

        composable(Screen.GoalSetup.route) {
            val viewModel: GoalSetupViewModel = hiltViewModel()
            GoalSetupScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onShowMessage = ::showToast
            )
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

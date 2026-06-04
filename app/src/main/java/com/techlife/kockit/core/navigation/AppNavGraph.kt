package com.techlife.kockit.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techlife.kockit.feature.auth.forgotpassword.ForgotPasswordScreen
import com.techlife.kockit.feature.auth.login.LoginScreen
import com.techlife.kockit.feature.auth.login.LoginViewModel
import com.techlife.kockit.feature.auth.register.RegisterScreen
import com.techlife.kockit.feature.auth.register.RegisterViewModel
import com.techlife.kockit.feature.goalsetup.GoalSetupScreen
import com.techlife.kockit.feature.goalsetup.GoalSetupViewModel
import com.techlife.kockit.feature.main.MainScreen
import com.techlife.kockit.feature.placementtest.PlacementTestExamScreen
import com.techlife.kockit.feature.placementtest.PlacementTestInfoScreen
import com.techlife.kockit.feature.placementtest.PlacementTestResultScreen
import com.techlife.kockit.feature.placementtest.PlacementTestSection
import com.techlife.kockit.feature.placementtest.PlacementTestViewModel
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
        startDestination = Screen.Main.route
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
                    navController.navigate(Screen.Placement.route) {
                        popUpTo(Screen.GoalSetup.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onShowMessage = ::showToast
            )
        }

        navigation(
            route = Screen.Placement.route,
            startDestination = Screen.placementInfo(PlacementTestSection.GENERAL_ABILITY.routeKey)
        ) {
            composable(
                route = Screen.PLACEMENT_INFO_ROUTE,
                arguments = listOf(
                    navArgument(Screen.PLACEMENT_SECTION_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val sectionKey = backStackEntry.arguments?.getString(Screen.PLACEMENT_SECTION_ARG).orEmpty()
                val section = PlacementTestSection.fromRouteKey(sectionKey)
                PlacementTestInfoScreen(
                    section = section,
                    onStartExam = {
                        navController.navigate(Screen.placementExam(section.routeKey))
                    }
                )
            }

            composable(
                route = Screen.PLACEMENT_EXAM_ROUTE,
                arguments = listOf(
                    navArgument(Screen.PLACEMENT_SECTION_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val sectionKey = backStackEntry.arguments?.getString(Screen.PLACEMENT_SECTION_ARG).orEmpty()
                val section = PlacementTestSection.fromRouteKey(sectionKey)
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.Placement.route)
                }
                val viewModel: PlacementTestViewModel = hiltViewModel(parentEntry)

                LaunchedEffect(section) {
                    viewModel.loadExam(section)
                }

                PlacementTestExamScreen(
                    section = section,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onFinishExam = {
                        navController.navigate(Screen.placementResult(section.routeKey))
                    }
                )
            }

            composable(
                route = Screen.PLACEMENT_RESULT_ROUTE,
                arguments = listOf(
                    navArgument(Screen.PLACEMENT_SECTION_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val sectionKey = backStackEntry.arguments?.getString(Screen.PLACEMENT_SECTION_ARG).orEmpty()
                val section = PlacementTestSection.fromRouteKey(sectionKey)

                PlacementTestResultScreen(
                    section = section,
                    onBackClick = { navController.popBackStack() },
                    onContinue = {
                        when (section) {
                            PlacementTestSection.GENERAL_ABILITY -> {
                                navController.navigate(
                                    Screen.placementInfo(PlacementTestSection.GENERAL_CULTURE.routeKey)
                                ) {
                                    popUpTo(Screen.placementInfo(section.routeKey)) { inclusive = true }
                                }
                            }
                            PlacementTestSection.GENERAL_CULTURE -> {
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Placement.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }

        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}

package com.techlife.kockit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.layout.HomeContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberHomeLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlacement: (sectionKey: String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onStudyPlanClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onMenuClick: () -> Unit = remember(scope, drawerState) {
        {
            scope.launch { drawerState.open() }
            Unit
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                is HomeEffect.NavigateToPlacement -> onNavigateToPlacement(effect.sectionKey)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                userName = uiState.fullName.ifBlank { HomeFakeData.USER_NAME },
                onStudyPlanClick = {
                    scope.launch { drawerState.close() }
                    onStudyPlanClick()
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    viewModel.onLogoutClick()
                }
            )
        }
    ) {
        val metrics = rememberHomeLayoutMetrics()
        val placementPending = uiState.showPlacementReminderCard

        HomeContentContainer(metrics = metrics) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBackground),
                verticalArrangement = Arrangement.spacedBy(metrics.sectionSpacing)
            ) {
                item(key = "top_bar") {
                    Spacer(modifier = Modifier.height(metrics.topInset))
                    HomeTopBar(
                        notificationCount = HomeFakeData.NOTIFICATION_COUNT,
                        onMenuClick = onMenuClick,
                        onSearchClick = onSearchClick
                    )
                }
                item(key = "greeting") {
                    HomeGreetingSection(
                        userName = uiState.fullName.ifBlank { HomeFakeData.USER_NAME }
                    )
                }
                if (!placementPending) {
                    item(key = "daily_goal") {
                        HomeDailyGoalCard(
                            completedNet = HomeFakeData.DAILY_GOAL_COMPLETED,
                            totalNet = HomeFakeData.DAILY_GOAL_TOTAL,
                            remainingNet = HomeFakeData.DAILY_GOAL_REMAINING
                        )
                    }
                }
                if (placementPending) {
                    item(key = "placement_reminder") {
                        HomePlacementReminderCard(
                            onClick = viewModel::onPlacementReminderClick,
                            remainingCount = uiState.remainingPlacementCount
                        )
                    }
                }
                if (!placementPending) {
                    item(key = "stats_carousel") {
                        HomeStatsCarousel()
                    }
                    if (metrics.useTwoColumnInsights) {
                        item(key = "priority_performance") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(metrics.sectionSpacing)
                            ) {
                                HomePriorityCard(
                                    lessons = HomeFakeData.priorityLessons,
                                    modifier = Modifier.weight(1f)
                                )
                                HomePerformanceCard(modifier = Modifier.weight(1f))
                            }
                        }
                    } else {
                        item(key = "priority") {
                            HomePriorityCard(lessons = HomeFakeData.priorityLessons)
                        }
                        item(key = "performance") {
                            HomePerformanceCard()
                        }
                    }
                    item(key = "goal_banner") {
                        HomeGoalBannerCard()
                        Spacer(modifier = Modifier.height(metrics.topInset))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
fun HomeScreenTabletPreview() {
    KocKitTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KocKitTheme {
        HomeScreen()
    }
}

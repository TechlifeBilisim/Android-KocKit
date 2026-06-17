package com.techlife.kockit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val onMenuClick: () -> Unit = remember(scope, drawerState) {
        {
            scope.launch { drawerState.open() }
            Unit
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(userName = HomeFakeData.USER_NAME)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "top_bar") {
                Spacer(modifier = Modifier.height(8.dp))
                HomeTopBar(
                    notificationCount = HomeFakeData.NOTIFICATION_COUNT,
                    onMenuClick = onMenuClick
                )
            }
            item(key = "greeting") {
                HomeGreetingSection(userName = HomeFakeData.USER_NAME)
            }
            item(key = "daily_goal") {
                HomeDailyGoalCard(
                    completedNet = HomeFakeData.DAILY_GOAL_COMPLETED,
                    totalNet = HomeFakeData.DAILY_GOAL_TOTAL,
                    remainingNet = HomeFakeData.DAILY_GOAL_REMAINING
                )
            }
            item(key = "stats_carousel") {
                HomeStatsCarousel()
            }
            item(key = "priority") {
                HomePriorityCard(lessons = HomeFakeData.priorityLessons)
            }
            item(key = "performance") {
                HomePerformanceCard()
            }
            item(key = "goal_banner") {
                HomeGoalBannerCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

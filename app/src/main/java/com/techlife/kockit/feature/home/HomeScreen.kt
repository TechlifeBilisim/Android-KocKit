package com.techlife.kockit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(userName = HomeFakeData.USER_NAME)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HomeTopBar(
                        notificationCount = HomeFakeData.NOTIFICATION_COUNT,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
                item {
                    HomeGreetingSection(userName = HomeFakeData.USER_NAME)
                }
                item {
                    HomeDailyGoalCard(
                        completedNet = HomeFakeData.DAILY_GOAL_COMPLETED,
                        totalNet = HomeFakeData.DAILY_GOAL_TOTAL,
                        remainingNet = HomeFakeData.DAILY_GOAL_REMAINING
                    )
                }
                item {
                    HomeStatsCarousel()
                }
                item {
                    HomePriorityCard(lessons = HomeFakeData.priorityLessons)
                }
                item {
                    HomePerformanceCard()
                }
                item {
                    HomeGoalBannerCard()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

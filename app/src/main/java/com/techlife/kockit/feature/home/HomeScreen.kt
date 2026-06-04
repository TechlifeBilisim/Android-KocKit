package com.techlife.kockit.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.CreamBackground

@Composable
fun HomeScreen() {
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
                HomeHeader(
                    userName = HomeFakeData.USER_NAME,
                    notificationCount = HomeFakeData.NOTIFICATION_COUNT
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeProgressCard(modifier = Modifier.weight(1f).fillMaxHeight())
                    HomePointsCard(modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
            item {
                HomePerformanceCard(modifier = Modifier.fillMaxWidth())
            }
            item {
                HomePriorityCard(lessons = HomeFakeData.priorityLessons)
            }
            item {
                HomeGoalBannerCard()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

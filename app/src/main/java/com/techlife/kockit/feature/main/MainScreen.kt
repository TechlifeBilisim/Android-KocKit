package com.techlife.kockit.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBottomNavigation
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.feature.home.HomeScreen
import com.techlife.kockit.feature.profile.ProfileScreen
import com.techlife.kockit.feature.search.SearchScreen
import com.techlife.kockit.feature.studyplan.StudyPlanScreen

@Composable
fun MainScreen(
    onNavigateToPlacement: (sectionKey: String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    var showSearch by rememberSaveable { mutableStateOf(false) }
    var showStudyPlan by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CreamBackground,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(
            WindowInsetsSides.Top + WindowInsetsSides.Horizontal
        ),
        bottomBar = {
            KocKitBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = {
                    showSearch = false
                    showStudyPlan = false
                    selectedTab = it
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.HOME -> {
                    when {
                        showSearch -> {
                            SearchScreen(onBackClick = { showSearch = false })
                        }
                        showStudyPlan -> {
                            StudyPlanScreen(onBackClick = { showStudyPlan = false })
                        }
                        else -> {
                            HomeScreen(
                                onNavigateToPlacement = onNavigateToPlacement,
                                onNavigateToLogin = onNavigateToLogin,
                                onSearchClick = { showSearch = true },
                                onStudyPlanClick = { showStudyPlan = true }
                            )
                        }
                    }
                }
                MainTab.EXAMS,
                MainTab.ANALYSIS,
                MainTab.GOALS -> MainTabPlaceholder(tab = selectedTab)
                MainTab.PROFILE -> ProfileScreen(
                    onBackClick = { selectedTab = MainTab.HOME }
                )
            }
        }
    }
}

@Composable
private fun MainTabPlaceholder(tab: MainTab) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KocKitSemiText(
            text = "${tab.label} yakında",
            color = TextSecondary
        )
    }
}

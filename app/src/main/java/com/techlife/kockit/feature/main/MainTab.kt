package com.techlife.kockit.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Description
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(
        label = "Anasayfa",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Filled.Home
    ),
    EXAMS(
        label = "Denemeler",
        selectedIcon = Icons.Filled.Description,
        unselectedIcon = Icons.Outlined.Description
    ),
    ANALYSIS(
        label = "Analiz",
        selectedIcon = Icons.Filled.Assessment,
        unselectedIcon = Icons.Filled.Assessment
    ),
    STUDY_PLAN(
        label = "Çalışma Planı",
        selectedIcon = Icons.Filled.CalendarToday,
        unselectedIcon = Icons.Outlined.CalendarToday
    ),
    PROFILE(
        label = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Filled.Person
    )
}

package com.techlife.kockit.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
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
        selectedIcon = Icons.Outlined.Description,
        unselectedIcon = Icons.Outlined.Description
    ),
    ANALYSIS(
        label = "Analiz",
        selectedIcon = Icons.Filled.Assessment,
        unselectedIcon = Icons.Filled.Assessment
    ),
    GOALS(
        label = "Hedefler",
        selectedIcon = Icons.Filled.TrackChanges,
        unselectedIcon = Icons.Filled.TrackChanges
    ),
    PROFILE(
        label = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Filled.Person
    )
}

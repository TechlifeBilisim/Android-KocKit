package com.techlife.kockit.feature.home

data class HomeUiState(
    val fullName: String = "",
    val examGoal: String = "",
    val university: String = "",
    val department: String = "",
    val showPlacementReminderCard: Boolean = true,
    val placementSectionKey: String? = null
)

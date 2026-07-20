package com.techlife.kockit.feature.profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val fullName: String = "",
    val rumuz: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "-",
    val school: String = "-",
    val profileImage: String? = null,
    val grade: String = "-",
    val examType: String = "-",
    val levelLabel: String = "Başarılı",
    val weeklyStudyHours: String = "-",
    val weeklyStudyProgress: Float = 0f,
    val weeklyStudyPercent: String = "-",
    val dailyStudyHours: String = "-"
)

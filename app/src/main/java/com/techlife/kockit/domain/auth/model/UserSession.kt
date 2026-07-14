package com.techlife.kockit.domain.auth.model

data class UserSession(
    val isFirstLaunch: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isOnboardingCompleted: Boolean = false,
    val kullaniciId: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val selectedExamGoal: String? = null,
    val selectedUniversity: String? = null,
    val selectedDepartment: String? = null
)

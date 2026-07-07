package com.techlife.kockit.domain.onboarding.model

data class OnboardingInfo(
    val examGoal: ExamGoal,
    val university: University,
    val department: Department,
    val gender: Gender? = null
)

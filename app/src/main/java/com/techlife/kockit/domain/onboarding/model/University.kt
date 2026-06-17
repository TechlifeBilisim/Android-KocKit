package com.techlife.kockit.domain.onboarding.model

data class University(
    val id: String,
    val name: String,
    val city: String,
    val region: String,
    val type: UniversityType
)

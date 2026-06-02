package com.techlife.kockit.data.onboarding.local

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {
    suspend fun saveOnboardingInfo(onboardingInfo: OnboardingInfo) {
        userPreferences.saveOnboardingSelections(
            examGoal = onboardingInfo.examGoal.title,
            university = onboardingInfo.university.name,
            department = onboardingInfo.department.name
        )
        userPreferences.setOnboardingCompleted(true)
    }
}

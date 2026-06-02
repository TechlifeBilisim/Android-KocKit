package com.techlife.kockit.domain.onboarding.repository

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University

interface OnboardingRepository {
    suspend fun getExamGoals(): List<ExamGoal>
    suspend fun getUniversities(): List<University>
    suspend fun getDepartments(): List<Department>
    suspend fun saveOnboardingInfo(onboardingInfo: OnboardingInfo): Result<Unit>
}

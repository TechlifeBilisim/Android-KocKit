package com.techlife.kockit.data.onboarding.repository

import com.techlife.kockit.data.onboarding.local.FakeOnboardingDataSource
import com.techlife.kockit.data.onboarding.local.OnboardingLocalDataSource
import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val fakeOnboardingDataSource: FakeOnboardingDataSource,
    private val onboardingLocalDataSource: OnboardingLocalDataSource
) : OnboardingRepository {

    override suspend fun getExamGoals(): List<ExamGoal> =
        fakeOnboardingDataSource.getExamGoals()

    override suspend fun getUniversities(): List<University> =
        fakeOnboardingDataSource.getUniversities()

    override suspend fun getDepartments(): List<Department> =
        fakeOnboardingDataSource.getDepartments()

    override suspend fun saveOnboardingInfo(onboardingInfo: OnboardingInfo): Result<Unit> =
        runCatching {
            onboardingLocalDataSource.saveOnboardingInfo(onboardingInfo)
        }
}

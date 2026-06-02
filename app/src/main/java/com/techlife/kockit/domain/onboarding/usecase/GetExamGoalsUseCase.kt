package com.techlife.kockit.domain.onboarding.usecase

import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class GetExamGoalsUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): List<ExamGoal> = onboardingRepository.getExamGoals()
}

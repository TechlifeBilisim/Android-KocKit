package com.techlife.kockit.domain.onboarding.usecase

import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class GetUniversitiesUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): List<University> = onboardingRepository.getUniversities()
}

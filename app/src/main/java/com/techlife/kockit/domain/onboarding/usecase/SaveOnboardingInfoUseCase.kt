package com.techlife.kockit.domain.onboarding.usecase

import com.techlife.kockit.domain.onboarding.model.OnboardingInfo
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class SaveOnboardingInfoUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(onboardingInfo: OnboardingInfo): Result<Unit> =
        onboardingRepository.saveOnboardingInfo(onboardingInfo)
}

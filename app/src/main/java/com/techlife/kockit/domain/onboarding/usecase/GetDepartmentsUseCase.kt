package com.techlife.kockit.domain.onboarding.usecase

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class GetDepartmentsUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): List<Department> = onboardingRepository.getDepartments()
}

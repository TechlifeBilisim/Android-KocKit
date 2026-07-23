package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.model.StartupDestination
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetStartupDestinationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val ensureValidAccessTokenUseCase: EnsureValidAccessTokenUseCase
) {
    suspend operator fun invoke(): StartupDestination {
        if (!ensureValidAccessTokenUseCase()) return StartupDestination.Login

        val session = authRepository.getCurrentSession()
        return if (session.isOnboardingCompleted) {
            StartupDestination.Main
        } else {
            StartupDestination.GoalSetup
        }
    }
}

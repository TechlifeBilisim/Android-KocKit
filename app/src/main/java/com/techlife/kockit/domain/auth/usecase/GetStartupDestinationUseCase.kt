package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.StartupDestination
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetStartupDestinationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): StartupDestination {
        if (!ensureAccessToken()) return StartupDestination.Login

        val session = authRepository.getCurrentSession()
        return if (session.isOnboardingCompleted) {
            StartupDestination.Main
        } else {
            StartupDestination.GoalSetup
        }
    }

    private suspend fun ensureAccessToken(): Boolean {
        if (!authRepository.getAccessToken().isNullOrBlank()) return true

        val refreshToken = authRepository.getRefreshToken()?.takeIf { it.isNotBlank() } ?: return false
        return when (authRepository.refreshToken()) {
            is ApiResult.Success -> !authRepository.getAccessToken().isNullOrBlank()
            is ApiResult.Error -> false
        }
    }
}

package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class CheckSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserSession = authRepository.getCurrentSession()
}

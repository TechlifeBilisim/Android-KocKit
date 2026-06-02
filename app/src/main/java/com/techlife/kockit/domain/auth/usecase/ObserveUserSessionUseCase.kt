package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserSession> = authRepository.observeUserSession()
}

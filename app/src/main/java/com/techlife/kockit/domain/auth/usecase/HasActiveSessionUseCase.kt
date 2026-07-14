package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class HasActiveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /** Kayıtlı access token varsa true — splash yönlendirmesi için. */
    suspend operator fun invoke(): Boolean {
        val token = authRepository.getAccessToken()
        return !token.isNullOrBlank()
    }
}

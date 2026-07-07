package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class HasActiveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        val session = authRepository.getCurrentSession()
        val token = authRepository.getAccessToken()
        // Otomatik giriş yalnızca "Beni hatırla" seçilmişse yapılır.
        return session.isLoggedIn && !token.isNullOrBlank() && authRepository.isRemembered()
    }
}

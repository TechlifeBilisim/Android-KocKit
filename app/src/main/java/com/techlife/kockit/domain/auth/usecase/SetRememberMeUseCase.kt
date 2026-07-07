package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SetRememberMeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(remember: Boolean, phone: String?) =
        authRepository.setRememberMe(remember, phone)
}

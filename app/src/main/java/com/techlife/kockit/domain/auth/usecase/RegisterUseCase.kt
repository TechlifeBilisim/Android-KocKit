package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(registerInfo: RegisterInfo): Result<Unit> =
        authRepository.register(registerInfo)
}

package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class VerifySmsCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): ApiResult<Unit> =
        authRepository.verifySmsCode(phone, code)
}

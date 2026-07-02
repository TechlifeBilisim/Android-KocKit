package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(registerInfo: RegisterInfo): ApiResult<RegisterResult> =
        authRepository.register(registerInfo)
}

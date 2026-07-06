package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(oAuthIdToken: String, email: String): ApiResult<LoginResult> =
        authRepository.loginWithGoogle(oAuthIdToken, email)
}

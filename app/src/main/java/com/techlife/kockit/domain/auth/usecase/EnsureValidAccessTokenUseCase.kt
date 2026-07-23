package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.core.network.auth.JwtExpiry
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

/**
 * Access token geçerliyse true.
 * Süresi dolmuşsa / yoksa refresh token ile yeniler; başarılıysa true, değilse false.
 */
class EnsureValidAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        val accessToken = authRepository.getAccessToken()
        if (JwtExpiry.isValid(accessToken)) return true

        if (authRepository.getRefreshToken().isNullOrBlank()) return false

        return when (authRepository.refreshToken()) {
            is ApiResult.Success -> JwtExpiry.isValid(authRepository.getAccessToken())
            is ApiResult.Error -> false
        }
    }
}

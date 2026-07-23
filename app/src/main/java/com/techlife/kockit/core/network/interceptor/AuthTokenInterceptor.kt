package com.techlife.kockit.core.network.interceptor

import com.techlife.kockit.core.network.auth.AuthTokenRefresher
import com.techlife.kockit.core.network.auth.JwtExpiry
import com.techlife.kockit.core.network.auth.TokenProvider
import com.techlife.kockit.core.network.config.AuthExemptPaths
import com.techlife.kockit.core.network.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authTokenRefresher: AuthTokenRefresher
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Giriş/kayıt/SMS/token yenile: Authorization gönderilmez.
        if (AuthExemptPaths.shouldSkipAuthorization(originalRequest.url.encodedPath)) {
            return chain.proceed(originalRequest)
        }

        val cached = tokenProvider.getAccessToken()?.takeIf { it.isNotBlank() }
        val token = when {
            JwtExpiry.isValid(cached) -> cached
            else -> authTokenRefresher.refreshAccessToken(force = true)
                ?.takeIf { it.isNotBlank() }
        }

        val request = if (token == null) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .header(
                    NetworkConfig.HEADER_AUTHORIZATION,
                    "${NetworkConfig.BEARER_PREFIX}$token"
                )
                .build()
        }

        return chain.proceed(request)
    }
}

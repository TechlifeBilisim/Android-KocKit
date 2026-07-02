package com.techlife.kockit.core.network.interceptor

import com.techlife.kockit.core.network.auth.TokenProvider
import com.techlife.kockit.core.network.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider.getAccessToken()

        val request = if (token.isNullOrBlank()) {
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

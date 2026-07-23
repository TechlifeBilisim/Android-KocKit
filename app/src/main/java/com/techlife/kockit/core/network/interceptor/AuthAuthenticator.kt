package com.techlife.kockit.core.network.interceptor

import com.techlife.kockit.core.network.auth.AuthTokenRefresher
import com.techlife.kockit.core.network.config.AuthExemptPaths
import com.techlife.kockit.core.network.config.NetworkConfig
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAuthenticator @Inject constructor(
    private val authTokenRefresher: AuthTokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        if (responseCount(response) >= 2) return null

        val path = response.request.url.encodedPath
        if (AuthExemptPaths.shouldSkipAuthorization(path)) return null

        val newToken = authTokenRefresher.refreshAccessToken(force = true) ?: return null
        return response.request.newBuilder()
            .header(
                NetworkConfig.HEADER_AUTHORIZATION,
                "${NetworkConfig.BEARER_PREFIX}$newToken"
            )
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

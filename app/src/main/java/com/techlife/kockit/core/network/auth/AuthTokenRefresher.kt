package com.techlife.kockit.core.network.auth

import com.techlife.kockit.BuildConfig
import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.core.network.interceptor.KocKitLoggingInterceptor
import com.techlife.kockit.data.remote.api.AuthApiService
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenRequestDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenRefresher @Inject constructor(
    private val userPreferences: UserPreferences,
    private val sessionTokenProvider: SessionTokenProvider,
    private val moshi: Moshi,
    loggingInterceptor: KocKitLoggingInterceptor
) {
    private val refreshApi: AuthApiService by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AuthApiService::class.java)
    }

    private val lock = Any()

    /**
     * Geçerli access token varsa onu döner.
     * Süresi dolmuşsa / yoksa refresh token ile [Token/Yenile] çağırır.
     */
    fun refreshAccessToken(force: Boolean = false): String? = synchronized(lock) {
        runBlocking {
            val currentAccess = sessionTokenProvider.getAccessToken()
                ?: userPreferences.getAccessToken()
            if (!force && JwtExpiry.isValid(currentAccess)) {
                return@runBlocking currentAccess
            }

            val refreshToken = userPreferences.getRefreshToken()?.takeIf { it.isNotBlank() }
                ?: return@runBlocking null

            try {
                val envelope = refreshApi.refreshToken(RefreshTokenRequestDto(refreshToken))
                val data = envelope.data
                if (!envelope.success || data == null || data.accessToken.isBlank()) {
                    userPreferences.clearSession()
                    sessionTokenProvider.updateCachedAccessToken(null)
                    return@runBlocking null
                }
                userPreferences.saveAuthTokens(data.accessToken, data.refreshToken)
                sessionTokenProvider.updateCachedAccessToken(data.accessToken)
                data.accessToken
            } catch (_: Exception) {
                null
            }
        }
    }
}

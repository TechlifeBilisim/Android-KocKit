package com.techlife.kockit.data.auth.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.auth.local.AuthLocalDataSource
import com.techlife.kockit.data.remote.datasource.AuthRemoteDataSource
import com.techlife.kockit.di.ApplicationScope
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.auth.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope
) : AuthRepository {

    override fun observeUserSession(): Flow<UserSession> =
        authLocalDataSource.observeUserSession()

    override suspend fun getCurrentSession(): UserSession =
        authLocalDataSource.getUserSession()

    override suspend fun getAccessToken(): String? =
        authLocalDataSource.getAccessToken()

    override suspend fun setRememberMe(remember: Boolean, phone: String?) =
        authLocalDataSource.setRememberMe(remember, phone)

    override suspend fun isRemembered(): Boolean = authLocalDataSource.isRemembered()

    override suspend fun getRememberedPhone(): String? = authLocalDataSource.getRememberedPhone()

    override suspend fun loginWithNickname(nickname: String, password: String): ApiResult<LoginResult> {
        return when (val result = authRemoteDataSource.loginWithNickname(nickname, password)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistLogin(result.data, password)
                result
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun requestLoginSms(phone: String): ApiResult<Unit> =
        authRemoteDataSource.requestLoginSms(phone)

    override suspend fun loginWithSms(phone: String, code: String): ApiResult<LoginResult> {
        return when (val result = authRemoteDataSource.loginWithSms(phone, code)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistLogin(result.data)
                result
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun loginWithGoogle(oAuthIdToken: String, email: String): ApiResult<LoginResult> {
        return when (val result = authRemoteDataSource.loginWithGoogle(oAuthIdToken, email)) {
            is ApiResult.Success -> {
                val loginResult = result.data
                if (loginResult.accessToken.isNullOrBlank()) {
                    ApiResult.Error(
                        message = when (loginResult.registered) {
                            false -> "Bu Google hesabıyla kayıt bulunamadı. Önce kayıt olun."
                            else -> "Google ile giriş tamamlanamadı."
                        }
                    )
                } else {
                    authLocalDataSource.persistLogin(loginResult)
                    result
                }
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun loginWithTechpass(xTechOturum: String): ApiResult<LoginResult> {
        return when (val result = authRemoteDataSource.loginWithTechpass(xTechOturum)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistLogin(result.data)
                result
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun refreshToken(): ApiResult<LoginResult> {
        val refreshToken = authLocalDataSource.getRefreshToken() ?: return ApiResult.Error(
            message = "Refresh token bulunamadı",
            code = 401
        )
        return when (val result = authRemoteDataSource.refreshToken(refreshToken)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistLogin(result.data)
                result
            }
            is ApiResult.Error -> {
                authLocalDataSource.logout()
                result
            }
        }
    }

    override suspend fun register(registerInfo: RegisterInfo): ApiResult<RegisterResult> {
        return when (val result = authRemoteDataSource.registerStudent(registerInfo)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistRegistration(registerInfo, result.data)
                result
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun sendSmsCode(phone: String): ApiResult<Unit> =
        authRemoteDataSource.sendSmsCode(phone)

    override suspend fun verifySmsCode(phone: String, code: String): ApiResult<Unit> =
        authRemoteDataSource.verifySmsCode(phone, code)

    override suspend fun sendEmailCode(email: String): ApiResult<Unit> =
        authRemoteDataSource.sendEmailCode(email)

    override suspend fun verifyEmailCode(email: String, code: String): ApiResult<Unit> =
        authRemoteDataSource.verifyEmailCode(email, code)

    override suspend fun logout() {
        // Sunucuya çıkış bildirimi (token hâlâ mevcutken) — best effort, uygulama kapansa
        // veya ağ yavaş olsa bile yerel temizliği bloklamaz.
        applicationScope.launch { authRemoteDataSource.logout() }
        // Yerel oturum ve token'lar her koşulda hemen ve garantili temizlenir.
        authLocalDataSource.logout()
    }

    override suspend fun setFirstLaunchCompleted() {
        authLocalDataSource.setFirstLaunchCompleted()
    }
}

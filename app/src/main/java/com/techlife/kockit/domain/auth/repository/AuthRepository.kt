package com.techlife.kockit.domain.auth.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeUserSession(): Flow<UserSession>
    suspend fun getCurrentSession(): UserSession
    suspend fun loginWithNickname(nickname: String, password: String): ApiResult<LoginResult>
    suspend fun requestLoginSms(phone: String): ApiResult<Unit>
    suspend fun loginWithSms(phone: String, code: String): ApiResult<LoginResult>
    suspend fun loginWithGoogle(oAuthIdToken: String, email: String): ApiResult<LoginResult>
    suspend fun loginWithTechpass(xTechOturum: String): ApiResult<LoginResult>
    suspend fun refreshToken(): ApiResult<LoginResult>
    suspend fun register(registerInfo: RegisterInfo): ApiResult<RegisterResult>
    suspend fun sendSmsCode(phone: String): ApiResult<Unit>
    suspend fun verifySmsCode(phone: String, code: String): ApiResult<Unit>
    suspend fun sendEmailCode(email: String): ApiResult<Unit>
    suspend fun verifyEmailCode(email: String, code: String): ApiResult<Unit>
    suspend fun logout()
    suspend fun setFirstLaunchCompleted()
}

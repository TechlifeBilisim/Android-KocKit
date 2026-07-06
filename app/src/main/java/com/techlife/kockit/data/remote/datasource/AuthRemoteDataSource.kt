package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.AuthApiService
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenRequestDto
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.mapper.toGoogleLoginRequestDto
import com.techlife.kockit.data.remote.mapper.toLoginSmsRequestDto
import com.techlife.kockit.data.remote.mapper.toLoginSmsVerifyRequestDto
import com.techlife.kockit.data.remote.mapper.toNicknameLoginRequestDto
import com.techlife.kockit.data.remote.mapper.toSendEmailCodeRequestDto
import com.techlife.kockit.data.remote.mapper.toSendSmsCodeRequestDto
import com.techlife.kockit.data.remote.mapper.toStudentRegisterRequestDto
import com.techlife.kockit.data.remote.mapper.toTechpassLoginRequestDto
import com.techlife.kockit.data.remote.mapper.toVerifyEmailCodeRequestDto
import com.techlife.kockit.data.remote.mapper.toVerifySmsCodeRequestDto
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.data.remote.util.requireSuccess
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val authApi: AuthApiService = apiServiceFactory.create()

    suspend fun registerStudent(registerInfo: RegisterInfo): ApiResult<RegisterResult> = execute {
        authApi.registerStudent(registerInfo.toStudentRegisterRequestDto()).requireData().toDomain()
    }

    suspend fun loginWithNickname(nickname: String, password: String): ApiResult<LoginResult> = execute {
        authApi.loginWithNickname(toNicknameLoginRequestDto(nickname, password)).requireData().toDomain()
    }

    suspend fun requestLoginSms(phone: String): ApiResult<Unit> = execute {
        authApi.requestLoginSms(phone.toLoginSmsRequestDto()).requireSuccess()
    }

    suspend fun loginWithSms(phone: String, code: String): ApiResult<LoginResult> = execute {
        authApi.loginWithSms(toLoginSmsVerifyRequestDto(phone, code)).requireData().toDomain()
    }

    suspend fun loginWithGoogle(oAuthIdToken: String, email: String): ApiResult<LoginResult> = execute {
        authApi.loginWithGoogle(toGoogleLoginRequestDto(oAuthIdToken, email)).requireData().toDomain()
    }

    suspend fun loginWithTechpass(xTechOturum: String): ApiResult<LoginResult> = execute {
        authApi.loginWithTechpass(toTechpassLoginRequestDto(xTechOturum)).requireData().toDomain()
    }

    suspend fun refreshToken(refreshToken: String): ApiResult<LoginResult> = execute {
        authApi.refreshToken(RefreshTokenRequestDto(refreshToken)).requireData().toDomain()
    }

    suspend fun sendSmsCode(phone: String): ApiResult<Unit> = execute {
        authApi.sendSmsCode(phone.toSendSmsCodeRequestDto()).requireSuccess()
    }

    suspend fun verifySmsCode(phone: String, code: String): ApiResult<Unit> = execute {
        authApi.verifySmsCode(toVerifySmsCodeRequestDto(phone, code)).requireSuccess()
    }

    suspend fun sendEmailCode(email: String): ApiResult<Unit> = execute {
        authApi.sendEmailCode(email.toSendEmailCodeRequestDto()).requireSuccess()
    }

    suspend fun verifyEmailCode(email: String, code: String): ApiResult<Unit> = execute {
        authApi.verifyEmailCode(toVerifyEmailCodeRequestDto(email, code)).requireSuccess()
    }

    suspend fun logout(): ApiResult<Unit> = execute {
        authApi.logout().requireSuccess()
    }
}

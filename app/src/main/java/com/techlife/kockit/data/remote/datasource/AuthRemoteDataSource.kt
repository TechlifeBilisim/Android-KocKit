package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.AuthApiService
import com.techlife.kockit.data.remote.dto.auth.LoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginResponseDto
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.mapper.toRegisterRequestDto
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val authApi: AuthApiService = apiServiceFactory.create()

    suspend fun login(email: String, password: String): ApiResult<LoginResponseDto> = execute {
        authApi.login(LoginRequestDto(email = email, password = password))
    }

    suspend fun register(registerInfo: RegisterInfo): ApiResult<RegisterResult> = execute {
        authApi.registerUser(registerInfo.toRegisterRequestDto()).requireData().toDomain()
    }
}

package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.auth.LoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginResponseDto
import com.techlife.kockit.data.remote.dto.auth.RegisterUserRequestDto
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.auth.RegisterUserResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    @ApiLog(ApiServices.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("api/yonetim/kullanici/kayit")
    @ApiLog(ApiServices.AUTH_REGISTER)
    suspend fun registerUser(
        @Body request: RegisterUserRequestDto
    ): ApiEnvelopeDto<RegisterUserResponseDto>
}

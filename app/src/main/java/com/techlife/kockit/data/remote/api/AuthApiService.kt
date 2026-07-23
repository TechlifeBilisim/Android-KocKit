package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.auth.AuthSessionDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginResponseDto
import com.techlife.kockit.data.remote.dto.auth.GoogleRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyResponseDto
import com.techlife.kockit.data.remote.dto.auth.NicknameLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenResponseDto
import com.techlife.kockit.data.remote.dto.auth.SendSmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterResponseDto
import com.techlife.kockit.data.remote.dto.auth.TechpassLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifySmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.common.EmptyDataDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("Api/Yonetim/Kullanici/Kayit/Ogrenci")
    @ApiLog(ApiServices.AUTH_STUDENT_REGISTER)
    suspend fun registerStudent(
        @Body request: StudentRegisterRequestDto
    ): ApiEnvelopeDto<StudentRegisterResponseDto>

    @POST("Api/Yonetim/Kullanici/Kayit/Google")
    @ApiLog(ApiServices.AUTH_GOOGLE_REGISTER)
    suspend fun registerWithGoogle(
        @Body request: GoogleRegisterRequestDto
    ): ApiEnvelopeDto<GoogleLoginResponseDto>

    @POST("Api/Yonetim/Kullanici/Giris/Rumuz")
    @ApiLog(ApiServices.AUTH_LOGIN_NICKNAME)
    suspend fun loginWithNickname(
        @Body request: NicknameLoginRequestDto
    ): ApiEnvelopeDto<AuthSessionDto>

    @POST("Api/Yonetim/Kullanici/Giris/CepTelefon")
    @ApiLog(ApiServices.AUTH_LOGIN_SMS_REQUEST)
    suspend fun requestLoginSms(
        @Body request: LoginSmsRequestDto
    ): ApiEnvelopeDto<LoginSmsVerifyResponseDto>

    @POST("Api/Yonetim/Kullanici/Giris/Sms/Dogrula")
    @ApiLog(ApiServices.AUTH_LOGIN_SMS_VERIFY)
    suspend fun loginWithSms(
        @Body request: LoginSmsVerifyRequestDto
    ): ApiEnvelopeDto<LoginSmsVerifyResponseDto>

    @POST("Api/Yonetim/Kullanici/Giris/Google")
    @ApiLog(ApiServices.AUTH_LOGIN_GOOGLE)
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginRequestDto
    ): ApiEnvelopeDto<GoogleLoginResponseDto>

    @POST("Api/Yonetim/Kullanici/Giris/Techpass")
    @ApiLog(ApiServices.AUTH_LOGIN_TECHPASS)
    suspend fun loginWithTechpass(
        @Body request: TechpassLoginRequestDto
    ): ApiEnvelopeDto<AuthSessionDto>

    @POST("Api/Yonetim/Kullanici/Token/Yenile")
    @ApiLog(ApiServices.AUTH_TOKEN_REFRESH)
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): ApiEnvelopeDto<RefreshTokenResponseDto>

    @POST("Api/Yonetim/Kullanici/Cikis")
    @ApiLog(ApiServices.AUTH_LOGOUT)
    suspend fun logout(): ApiEnvelopeDto<EmptyDataDto?>

    @POST("Api/Yonetim/Kullanici/Sms/KodGonder")
    @ApiLog(ApiServices.AUTH_SMS_SEND_CODE)
    suspend fun sendSmsCode(
        @Body request: SendSmsCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>

    @POST("Api/Yonetim/Kullanici/Sms/KodDogrula")
    @ApiLog(ApiServices.AUTH_SMS_VERIFY_CODE)
    suspend fun verifySmsCode(
        @Body request: VerifySmsCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>
}

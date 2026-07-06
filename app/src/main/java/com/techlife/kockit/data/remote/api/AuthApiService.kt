package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.auth.AuthSessionDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginResponseDto
import com.techlife.kockit.data.remote.dto.auth.LoginInitResponseDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyRequestDto
import com.techlife.kockit.data.remote.dto.auth.NicknameLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenResponseDto
import com.techlife.kockit.data.remote.dto.auth.SendEmailCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.SendSmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterResponseDto
import com.techlife.kockit.data.remote.dto.auth.TechpassLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifyEmailCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifySmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.common.EmptyDataDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/yonetim/kullanici/ogrenci/kayit")
    @ApiLog(ApiServices.AUTH_STUDENT_REGISTER)
    suspend fun registerStudent(
        @Body request: StudentRegisterRequestDto
    ): ApiEnvelopeDto<StudentRegisterResponseDto>

    @POST("api/yonetim/kullanici/giris/rumuz")
    @ApiLog(ApiServices.AUTH_LOGIN_NICKNAME)
    suspend fun loginWithNickname(
        @Body request: NicknameLoginRequestDto
    ): ApiEnvelopeDto<AuthSessionDto>

    @POST("api/yonetim/kullanici/giris/sms")
    @ApiLog(ApiServices.AUTH_LOGIN_SMS_REQUEST)
    suspend fun requestLoginSms(
        @Body request: LoginSmsRequestDto
    ): ApiEnvelopeDto<LoginInitResponseDto>

    @POST("api/yonetim/kullanici/giris/sms-dogrula")
    @ApiLog(ApiServices.AUTH_LOGIN_SMS_VERIFY)
    suspend fun loginWithSms(
        @Body request: LoginSmsVerifyRequestDto
    ): ApiEnvelopeDto<AuthSessionDto>

    @POST("api/yonetim/kullanici/giris/google")
    @ApiLog(ApiServices.AUTH_LOGIN_GOOGLE)
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginRequestDto
    ): ApiEnvelopeDto<GoogleLoginResponseDto>

    @POST("api/yonetim/kullanici/giris/techpass")
    @ApiLog(ApiServices.AUTH_LOGIN_TECHPASS)
    suspend fun loginWithTechpass(
        @Body request: TechpassLoginRequestDto
    ): ApiEnvelopeDto<AuthSessionDto>

    @POST("api/yonetim/kullanici/token/yenile")
    @ApiLog(ApiServices.AUTH_TOKEN_REFRESH)
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): ApiEnvelopeDto<RefreshTokenResponseDto>

    @POST("api/yonetim/kullanici/cikis")
    @ApiLog(ApiServices.AUTH_LOGOUT)
    suspend fun logout(): ApiEnvelopeDto<EmptyDataDto?>

    @POST("api/yonetim/kullanici/sms/kod-gonder")
    @ApiLog(ApiServices.AUTH_SMS_SEND_CODE)
    suspend fun sendSmsCode(
        @Body request: SendSmsCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>

    @POST("api/yonetim/kullanici/sms/kod-dogrula")
    @ApiLog(ApiServices.AUTH_SMS_VERIFY_CODE)
    suspend fun verifySmsCode(
        @Body request: VerifySmsCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>

    @POST("api/yonetim/kullanici/email/kod-gonder")
    @ApiLog(ApiServices.AUTH_EMAIL_SEND_CODE)
    suspend fun sendEmailCode(
        @Body request: SendEmailCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>

    @POST("api/yonetim/kullanici/email/kod-dogrula")
    @ApiLog(ApiServices.AUTH_EMAIL_VERIFY_CODE)
    suspend fun verifyEmailCode(
        @Body request: VerifyEmailCodeRequestDto
    ): ApiEnvelopeDto<EmptyDataDto?>
}

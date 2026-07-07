package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.core.util.normalizeTurkishPhone
import com.techlife.kockit.core.util.splitPersonName
import com.techlife.kockit.data.remote.auth.AuthLoginType
import com.techlife.kockit.data.remote.dto.auth.AuthSessionDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginResponseDto
import com.techlife.kockit.data.remote.dto.auth.LoginInitResponseDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyRequestDto
import com.techlife.kockit.data.remote.dto.auth.NicknameLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenResponseDto
import com.techlife.kockit.data.remote.dto.auth.SendEmailCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.SendSmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterResponseDto
import com.techlife.kockit.data.remote.dto.auth.TechpassLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifyEmailCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifySmsCodeRequestDto
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterAccountType
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult

object RegisterApiConstants {
    const val DEFAULT_CINSIYET_ID = 1
    const val DEFAULT_IL_ID = 1
    const val DEFAULT_ILCE_ID = 1
}

fun RegisterInfo.toStudentRegisterRequestDto(): StudentRegisterRequestDto {
    val (ad, soyad) = splitPersonName(fullName)
    return when (accountType) {
        RegisterAccountType.NICKNAME -> StudentRegisterRequestDto(
            ad = ad,
            soyad = soyad,
            loginTypeId = AuthLoginType.RUMUZ,
            rumuz = nickname.trim(),
            eposta = email.trim(),
            sifre = password,
            cinsiyetId = RegisterApiConstants.DEFAULT_CINSIYET_ID,
            ilId = RegisterApiConstants.DEFAULT_IL_ID,
            ilceId = RegisterApiConstants.DEFAULT_ILCE_ID
        )
        RegisterAccountType.PHONE -> StudentRegisterRequestDto(
            ad = ad,
            soyad = soyad,
            cepTelefon = normalizeTurkishPhone(phone),
            loginTypeId = AuthLoginType.TELEFON,
            eposta = email.trim(),
            sifre = password.ifBlank { null },
            rumuz = nickname.trim().ifBlank { null },
            cinsiyetId = RegisterApiConstants.DEFAULT_CINSIYET_ID,
            ilId = RegisterApiConstants.DEFAULT_IL_ID,
            ilceId = RegisterApiConstants.DEFAULT_ILCE_ID
        )
    }
}

fun StudentRegisterResponseDto.toDomain(): RegisterResult = RegisterResult(
    userId = kullaniciId,
    accessToken = accessToken,
    refreshToken = refreshToken,
    email = eposta,
    phone = cepTelefon
)

fun toNicknameLoginRequestDto(nickname: String, password: String): NicknameLoginRequestDto =
    NicknameLoginRequestDto(rumuz = nickname.trim(), sifre = password)

fun String.toLoginSmsRequestDto(): LoginSmsRequestDto =
    LoginSmsRequestDto(cepTelefon = normalizeTurkishPhone(this))

fun String.toSendSmsCodeRequestDto(): SendSmsCodeRequestDto =
    SendSmsCodeRequestDto(cepTelefon = normalizeTurkishPhone(this))

fun String.toSendEmailCodeRequestDto(): SendEmailCodeRequestDto =
    SendEmailCodeRequestDto(eposta = trim())

fun toVerifySmsCodeRequestDto(phone: String, code: String): VerifySmsCodeRequestDto =
    VerifySmsCodeRequestDto(cepTelefon = normalizeTurkishPhone(phone), kod = code)

fun toLoginSmsVerifyRequestDto(phone: String, code: String): LoginSmsVerifyRequestDto =
    LoginSmsVerifyRequestDto(cepTelefon = normalizeTurkishPhone(phone), kod = code)

fun toVerifyEmailCodeRequestDto(email: String, code: String): VerifyEmailCodeRequestDto =
    VerifyEmailCodeRequestDto(eposta = email.trim(), kod = code)

fun toGoogleLoginRequestDto(oAuthIdToken: String, email: String): GoogleLoginRequestDto =
    GoogleLoginRequestDto(oAuthIdToken = oAuthIdToken, email = email.trim())

fun toTechpassLoginRequestDto(xTechOturum: String): TechpassLoginRequestDto =
    TechpassLoginRequestDto(xTechOturum = xTechOturum)

fun AuthSessionDto.toDomain(): LoginResult {
    val fullName = listOfNotNull(ad, soyad).joinToString(" ").takeIf { it.isNotBlank() }
    return LoginResult(
        accountVerified = true,
        registered = true,
        userId = kullaniciId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        fullName = fullName,
        email = eposta,
        phone = cepTelefon
    )
}

fun LoginInitResponseDto.toDomain(): LoginResult = toGoogleLoginDomain()

fun GoogleLoginResponseDto.toDomain(): LoginResult = toGoogleLoginDomain()

private fun GoogleLoginResponseDto.toGoogleLoginDomain(): LoginResult {
    val profile = kullaniciProfili?.toDomain()
        ?: if (!accessToken.isNullOrBlank()) {
            AuthSessionDto(
                kullaniciId = kullaniciId,
                ad = ad,
                soyad = soyad,
                resim = resim,
                eposta = eposta,
                cepTelefon = cepTelefon,
                cinsiyetId = cinsiyetId,
                accessToken = accessToken,
                refreshToken = refreshToken
            ).toDomain()
        } else {
            null
        }

    return profile?.copy(
        accountVerified = hesapOnaylandi ?: profile.accountVerified,
        registered = kayitli ?: profile.registered
    ) ?: LoginResult(
        accountVerified = hesapOnaylandi ?: false,
        registered = kayitli ?: false
    )
}

private fun LoginInitResponseDto.toGoogleLoginDomain(): LoginResult =
    GoogleLoginResponseDto(
        hesapOnaylandi = hesapOnaylandi,
        kayitli = kayitli,
        kullaniciProfili = kullaniciProfili
    ).toGoogleLoginDomain()

fun RefreshTokenResponseDto.toDomain(): LoginResult = LoginResult(
    accountVerified = true,
    registered = true,
    accessToken = accessToken,
    refreshToken = refreshToken
)

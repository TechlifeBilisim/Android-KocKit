package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.core.util.normalizeTurkishPhone
import com.techlife.kockit.core.util.splitPersonName
import com.techlife.kockit.data.remote.dto.auth.AuthSessionDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.GoogleLoginResponseDto
import com.techlife.kockit.data.remote.dto.auth.GoogleRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginInitResponseDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyRequestDto
import com.techlife.kockit.data.remote.dto.auth.LoginSmsVerifyResponseDto
import com.techlife.kockit.data.remote.dto.auth.NicknameLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.RefreshTokenResponseDto
import com.techlife.kockit.data.remote.dto.auth.SendSmsCodeRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterRequestDto
import com.techlife.kockit.data.remote.dto.auth.StudentRegisterResponseDto
import com.techlife.kockit.data.remote.dto.auth.TechpassLoginRequestDto
import com.techlife.kockit.data.remote.dto.auth.VerifySmsCodeRequestDto
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult

fun RegisterInfo.toStudentRegisterRequestDto(): StudentRegisterRequestDto {
    val (ad, soyad) = splitPersonName(fullName)
    return StudentRegisterRequestDto(
        ad = ad,
        soyad = soyad,
        cepTelefon = normalizeTurkishPhone(phone).ifBlank { null },
        rumuz = nickname.trim().ifBlank { null },
        eposta = email.trim(),
        cinsiyet = gender.apiId
    )
}

fun RegisterInfo.toGoogleRegisterRequestDto(oAuthIdToken: String): GoogleRegisterRequestDto {
    val (ad, soyad) = splitPersonName(fullName)
    return GoogleRegisterRequestDto(
        ad = ad,
        soyad = soyad,
        cepTelefon = normalizeTurkishPhone(phone).ifBlank { null },
        rumuz = nickname.trim().ifBlank { null },
        eposta = email.trim(),
        cinsiyet = gender.apiId,
        oAuthIdToken = oAuthIdToken
    )
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

fun toVerifySmsCodeRequestDto(phone: String, code: String): VerifySmsCodeRequestDto =
    VerifySmsCodeRequestDto(cepTelefon = normalizeTurkishPhone(phone), kod = code)

fun toLoginSmsVerifyRequestDto(phone: String, code: String): LoginSmsVerifyRequestDto =
    LoginSmsVerifyRequestDto(cepTelefon = normalizeTurkishPhone(phone), kod = code)

fun toGoogleLoginRequestDto(oAuthIdToken: String, email: String): GoogleLoginRequestDto =
    GoogleLoginRequestDto(oAuthIdToken = oAuthIdToken, email = email.trim())

fun toTechpassLoginRequestDto(xTechOturum: String): TechpassLoginRequestDto =
    TechpassLoginRequestDto(xTechOturum = xTechOturum)

fun LoginSmsVerifyResponseDto.toDomain(): LoginResult {
    val user = kullanici
    val fullName = listOfNotNull(user?.ad, user?.soyad).joinToString(" ").takeIf { it.isNotBlank() }
    return LoginResult(
        accountVerified = smsDogrulandi,
        registered = true,
        userId = user?.kullaniciId,
        accessToken = user?.accessToken,
        refreshToken = user?.refreshToken,
        fullName = fullName,
        email = user?.eposta,
        phone = null,
        hasStudentGoal = ogrenciHedefVarMi
    )
}

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

fun GoogleLoginResponseDto.toRegisterResult(): RegisterResult? {
    val token = accessToken ?: kullaniciProfili?.accessToken ?: return null
    val userId = kullaniciId ?: kullaniciProfili?.kullaniciId ?: return null
    return RegisterResult(
        userId = userId,
        accessToken = token,
        refreshToken = refreshToken ?: kullaniciProfili?.refreshToken,
        email = eposta ?: kullaniciProfili?.eposta,
        phone = cepTelefon ?: kullaniciProfili?.cepTelefon
    )
}

private fun GoogleLoginResponseDto.toGoogleLoginDomain(): LoginResult {
    val profile = kullaniciProfili
    val resolvedAccessToken = accessToken?.takeIf { it.isNotBlank() }
        ?: profile?.accessToken?.takeIf { it.isNotBlank() }
    val resolvedRefreshToken = refreshToken?.takeIf { it.isNotBlank() }
        ?: profile?.refreshToken?.takeIf { it.isNotBlank() }
    val resolvedUserId = kullaniciId ?: profile?.kullaniciId
    val resolvedAd = ad ?: profile?.ad
    val resolvedSoyad = soyad ?: profile?.soyad
    val fullName = listOfNotNull(resolvedAd, resolvedSoyad).joinToString(" ").takeIf { it.isNotBlank() }

    return LoginResult(
        accountVerified = hesapOnaylandi ?: !resolvedAccessToken.isNullOrBlank(),
        registered = kayitli ?: !resolvedAccessToken.isNullOrBlank(),
        userId = resolvedUserId,
        accessToken = resolvedAccessToken,
        refreshToken = resolvedRefreshToken,
        fullName = fullName,
        email = eposta ?: profile?.eposta,
        phone = cepTelefon ?: profile?.cepTelefon
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

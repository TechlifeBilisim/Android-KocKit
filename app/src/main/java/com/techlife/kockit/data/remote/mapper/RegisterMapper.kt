package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.core.util.normalizeTurkishPhone
import com.techlife.kockit.core.util.splitPersonName
import com.techlife.kockit.data.remote.dto.auth.RegisterUserRequestDto
import com.techlife.kockit.data.remote.dto.auth.RegisterUserResponseDto
import com.techlife.kockit.domain.auth.model.RegisterAccountType
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult

object RegisterApiConstants {
    const val LOGIN_TYPE_NICKNAME = 4
    const val LOGIN_TYPE_PHONE = 3
}

fun RegisterInfo.toRegisterRequestDto(): RegisterUserRequestDto {
    val (ad, soyad) = splitPersonName(fullName)
    return when (accountType) {
        RegisterAccountType.NICKNAME -> RegisterUserRequestDto(
            ad = ad,
            soyad = soyad,
            loginTypeId = RegisterApiConstants.LOGIN_TYPE_NICKNAME,
            rumuz = nickname.trim(),
            eposta = email.trim(),
            sifre = password
        )
        RegisterAccountType.PHONE -> RegisterUserRequestDto(
            ad = ad,
            soyad = soyad,
            cepTelefon = normalizeTurkishPhone(phone),
            loginTypeId = RegisterApiConstants.LOGIN_TYPE_PHONE,
            eposta = email.trim(),
            sifre = password
        )
    }
}

fun RegisterUserResponseDto.toDomain(): RegisterResult = RegisterResult(
    userId = kullaniciId,
    accessToken = accessToken,
    refreshToken = refreshToken
)

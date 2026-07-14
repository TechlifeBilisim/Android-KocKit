package com.techlife.kockit.feature.auth.register

import com.techlife.kockit.feature.auth.common.normalizeTurkishPhone

object RegisterSteps {
    const val ACCOUNT = 1
    const val OTP = 2
}

enum class RegisterAccountMethod {
    NICKNAME,
    PHONE
}

internal fun validateRegisterFullName(fullName: String): String? {
    val parts = fullName.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "Ad soyad gerekli"
        parts.size < 2 -> "Ad ve soyad gir"
        else -> null
    }
}

internal fun validateEmail(email: String): String? {
    val trimmed = email.trim()
    return when {
        trimmed.isBlank() -> "E-posta adresi gerekli"
        !trimmed.contains("@") || !trimmed.contains(".") -> "Geçerli bir e-posta adresi gir"
        else -> null
    }
}

internal fun maskPhoneDestination(phone: String): String {
    val normalized = normalizeTurkishPhone(phone)
    return if (normalized.length < 4) {
        ""
    } else {
        "+90 ${normalized.take(3)} *** ** ${normalized.takeLast(2)}"
    }
}

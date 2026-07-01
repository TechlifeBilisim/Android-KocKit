package com.techlife.kockit.feature.auth.common

fun normalizeTurkishPhone(raw: String): String {
    val digits = raw.filter(Char::isDigit)
    return when {
        digits.startsWith("90") && digits.length >= 12 -> digits.drop(2).take(10)
        digits.startsWith("0") && digits.length >= 11 -> digits.drop(1).take(10)
        else -> digits.take(10)
    }
}

fun validateTurkishPhone(raw: String): String? {
    val normalized = normalizeTurkishPhone(raw)
    return when {
        normalized.isBlank() -> "Telefon numarası gerekli"
        normalized.length != 10 -> "Geçerli bir telefon numarası gir"
        !normalized.startsWith("5") -> "Geçerli bir Türkiye cep telefonu gir"
        else -> null
    }
}

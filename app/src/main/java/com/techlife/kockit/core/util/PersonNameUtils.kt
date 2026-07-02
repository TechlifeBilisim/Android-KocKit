package com.techlife.kockit.core.util

fun normalizeTurkishPhone(raw: String): String {
    val digits = raw.filter(Char::isDigit)
    return when {
        digits.startsWith("90") && digits.length >= 12 -> digits.drop(2).take(10)
        digits.startsWith("0") && digits.length >= 11 -> digits.drop(1).take(10)
        else -> digits.take(10)
    }
}

fun splitPersonName(fullName: String): Pair<String, String> {
    val parts = fullName.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    if (parts.isEmpty()) return "" to ""
    val firstName = parts.first()
    val lastName = parts.drop(1).joinToString(" ")
    return firstName to lastName
}

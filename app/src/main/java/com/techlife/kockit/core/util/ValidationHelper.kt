package com.techlife.kockit.core.util

import com.techlife.kockit.core.common.Constants

object ValidationHelper {
    fun isValidEmail(email: String): Boolean {
        val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.isNotBlank() && pattern.matches(email.trim())
    }

    fun isValidPassword(password: String): Boolean =
        password.length >= Constants.MIN_PASSWORD_LENGTH

    fun isNotBlank(value: String): Boolean = value.isNotBlank()

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}

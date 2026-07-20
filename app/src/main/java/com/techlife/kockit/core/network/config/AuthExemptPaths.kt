package com.techlife.kockit.core.network.config

/**
 * Giriş, kayıt ve token yenileme isteklerine Authorization header eklenmez.
 * Bu uçlar token almak için kullanılır; mevcut access token gönderilmez.
 */
object AuthExemptPaths {
    fun shouldSkipAuthorization(encodedPath: String): Boolean {
        val path = encodedPath.lowercase()
        return path.contains("/giris/") ||
            path.contains("/kayit") ||
            path.contains("/token/yenile") ||
            path.contains("/sms/")
    }
}

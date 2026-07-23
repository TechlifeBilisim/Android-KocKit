package com.techlife.kockit.core.network.config

/**
 * Giriş, kayıt ve token yenileme isteklerine Authorization header eklenmez.
 *
 * Not: [Sms/KodGonder] ve [Sms/KodDogrula] kayıt sonrası auth ister;
 * bu yüzden genel `/sms/` yolu muaf tutulmaz. Login SMS uçları `/giris/` altındadır.
 */
object AuthExemptPaths {
    fun shouldSkipAuthorization(encodedPath: String): Boolean {
        val path = encodedPath.lowercase()
        return path.contains("/giris/") ||
            path.contains("/kayit") ||
            path.contains("/token/yenile")
    }
}

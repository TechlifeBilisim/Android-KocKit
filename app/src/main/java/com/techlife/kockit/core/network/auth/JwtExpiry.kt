package com.techlife.kockit.core.network.auth

import android.util.Base64
import org.json.JSONObject

/**
 * JWT payload içindeki `exp` claim'ine bakarak access token geçerliliğini kontrol eder.
 * İmza doğrulamaz; sadece yerel süre kontrolü içindir.
 */
object JwtExpiry {
    private const val EXPIRY_SKEW_SECONDS = 30L

    fun isExpiredOrInvalid(token: String?, nowEpochSeconds: Long = System.currentTimeMillis() / 1000): Boolean {
        if (token.isNullOrBlank()) return true
        val exp = readExpClaim(token) ?: return true
        return exp <= nowEpochSeconds + EXPIRY_SKEW_SECONDS
    }

    fun isValid(token: String?): Boolean = !isExpiredOrInvalid(token)

    private fun readExpClaim(token: String): Long? {
        val parts = token.split('.')
        if (parts.size < 2) return null
        return runCatching {
            val payloadJson = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
                Charsets.UTF_8
            )
            JSONObject(payloadJson).optLong("exp", -1L).takeIf { it > 0 }
        }.getOrNull()
    }
}

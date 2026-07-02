package com.techlife.kockit.core.network.model

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class ApiErrorBody(
    val message: String? = null,
    val error: String? = null,
    val detail: String? = null
)

fun Throwable.toApiResult(): ApiResult.Error {
    return when (this) {
        is ApiEnvelopeException -> ApiResult.Error(
            message = message ?: "İşlem başarısız",
            code = statusCode,
            cause = this
        )
        is HttpException -> {
            val code = code()
            val parsedMessage = response()?.errorBody()?.string()?.let(::parseErrorMessage)
            ApiResult.Error(
                message = parsedMessage ?: message(),
                code = code,
                cause = this
            )
        }
        is SocketTimeoutException -> ApiResult.Error(
            message = "İstek zaman aşımına uğradı.",
            cause = this
        )
        is UnknownHostException -> ApiResult.Error(
            message = "Sunucuya bağlanılamadı. İnternet bağlantınızı kontrol edin.",
            cause = this
        )
        is JsonDataException -> ApiResult.Error(
            message = "Sunucu yanıtı işlenemedi.",
            cause = this
        )
        is IOException -> ApiResult.Error(
            message = "Bağlantı hatası oluştu.",
            cause = this
        )
        else -> ApiResult.Error(
            message = localizedMessage?.takeIf { it.isNotBlank() } ?: "Beklenmeyen bir hata oluştu.",
            cause = this
        )
    }
}

private val errorBodyMoshi: Moshi = Moshi.Builder().build()

private fun parseErrorMessage(raw: String): String? {
    if (raw.isBlank()) return null
    return runCatching {
        val adapter = errorBodyMoshi.adapter(ApiErrorBody::class.java)
        val body = adapter.fromJson(raw)
        body?.message ?: body?.error ?: body?.detail
    }.getOrNull() ?: raw.takeIf { it.length <= 240 }
}

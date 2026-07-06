package com.techlife.kockit.data.remote.util

import com.techlife.kockit.core.network.model.ApiEnvelopeException
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto

fun <T> ApiEnvelopeDto<T>.requireData(): T {
    if (!success || data == null) {
        throw ApiEnvelopeException(
            message = message?.takeIf { it.isNotBlank() } ?: "İşlem başarısız",
            statusCode = statusCode
        )
    }
    return data
}

fun ApiEnvelopeDto<*>.requireSuccess() {
    if (!success) {
        throw ApiEnvelopeException(
            message = message?.takeIf { it.isNotBlank() } ?: "İşlem başarısız",
            statusCode = statusCode
        )
    }
}

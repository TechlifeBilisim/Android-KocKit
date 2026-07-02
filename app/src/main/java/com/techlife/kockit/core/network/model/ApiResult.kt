package com.techlife.kockit.core.network.model

import com.techlife.kockit.core.common.Resource
import com.techlife.kockit.core.common.UiText

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(
        val message: String,
        val code: Int? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>
}

fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data

fun <T> ApiResult<T>.toResource(
    errorMapper: (ApiResult.Error) -> UiText = { UiText.DynamicString(it.message) }
): Resource<T> = when (this) {
    is ApiResult.Success -> Resource.Success(data)
    is ApiResult.Error -> Resource.Error(errorMapper(this))
}

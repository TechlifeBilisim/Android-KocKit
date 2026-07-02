package com.techlife.kockit.core.network.util

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.core.network.model.toApiResult

suspend fun <T> safeApiCall(
    call: suspend () -> T
): ApiResult<T> = try {
    ApiResult.Success(call())
} catch (throwable: Throwable) {
    throwable.toApiResult()
}

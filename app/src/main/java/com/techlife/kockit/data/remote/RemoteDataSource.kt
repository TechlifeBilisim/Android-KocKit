package com.techlife.kockit.data.remote

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.core.network.util.safeApiCall

abstract class RemoteDataSource {
    protected suspend fun <T> execute(
        call: suspend () -> T
    ): ApiResult<T> = safeApiCall(call)
}

package com.techlife.kockit.data.remote.dto.common

data class ApiEnvelopeDto<T>(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val data: T?,
    val errors: Any?
)

package com.techlife.kockit.core.network.model

class ApiEnvelopeException(
    message: String,
    val statusCode: Int? = null
) : Exception(message)

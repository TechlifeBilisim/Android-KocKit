package com.techlife.kockit.core.network.auth

interface TokenProvider {
    fun getAccessToken(): String?
}

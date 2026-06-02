package com.techlife.kockit.core.common

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: UiText? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}

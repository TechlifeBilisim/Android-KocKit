package com.techlife.kockit.core.network.auth

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionTokenProvider @Inject constructor(
    private val userPreferences: UserPreferences,
    @ApplicationScope private val applicationScope: CoroutineScope
) : TokenProvider {

    @Volatile
    private var cachedAccessToken: String? = null

    init {
        applicationScope.launch {
            cachedAccessToken = userPreferences.getAccessToken()
        }
        userPreferences.accessTokenFlow
            .onEach { token -> cachedAccessToken = token }
            .launchIn(applicationScope)
    }

    override fun getAccessToken(): String? {
        cachedAccessToken?.let { return it }
        return runBlocking {
            userPreferences.getAccessToken()
        }.also { cachedAccessToken = it }
    }

    fun updateCachedAccessToken(token: String?) {
        cachedAccessToken = token
    }
}

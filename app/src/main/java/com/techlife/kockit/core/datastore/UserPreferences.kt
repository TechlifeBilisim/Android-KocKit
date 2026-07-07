package com.techlife.kockit.core.datastore

import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.placement.model.PlacementProgress

interface UserPreferences {
    val userSessionFlow: kotlinx.coroutines.flow.Flow<UserSession>
    val placementProgressFlow: kotlinx.coroutines.flow.Flow<PlacementProgress>
    val accessTokenFlow: kotlinx.coroutines.flow.Flow<String?>
    suspend fun getUserSession(): UserSession
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String? = null)
    suspend fun setFirstLaunch(isFirstLaunch: Boolean)
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun saveUserInfo(
        fullName: String?,
        email: String?,
        phoneNumber: String?
    )
    suspend fun saveOnboardingSelections(
        examGoal: String?,
        university: String?,
        department: String?
    )
    suspend fun savePassword(password: String)
    suspend fun getPassword(): String?
    suspend fun setPlacementSectionCompleted(sectionKey: String, completed: Boolean)
    suspend fun setRememberMe(remember: Boolean, phone: String?)
    suspend fun getRememberMe(): Boolean
    suspend fun getRememberedPhone(): String?
    suspend fun clearSession()
}

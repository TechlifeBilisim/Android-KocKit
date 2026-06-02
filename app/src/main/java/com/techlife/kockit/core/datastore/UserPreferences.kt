package com.techlife.kockit.core.datastore

import com.techlife.kockit.domain.auth.model.UserSession

interface UserPreferences {
    val userSessionFlow: kotlinx.coroutines.flow.Flow<UserSession>
    suspend fun getUserSession(): UserSession
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
    suspend fun clearSession()
}

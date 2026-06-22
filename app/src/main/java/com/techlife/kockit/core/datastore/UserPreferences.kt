package com.techlife.kockit.core.datastore

import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.placement.model.PlacementProgress

interface UserPreferences {
    val userSessionFlow: kotlinx.coroutines.flow.Flow<UserSession>
    val placementProgressFlow: kotlinx.coroutines.flow.Flow<PlacementProgress>
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
    suspend fun setPlacementSectionCompleted(sectionKey: String, completed: Boolean)
    suspend fun clearSession()
}

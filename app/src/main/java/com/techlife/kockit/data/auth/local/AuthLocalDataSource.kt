package com.techlife.kockit.data.auth.local

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.UserSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {
    fun observeUserSession(): Flow<UserSession> = userPreferences.userSessionFlow

    suspend fun getUserSession(): UserSession = userPreferences.getUserSession()

    suspend fun login(email: String, password: String): Result<Unit> {
        val storedPassword = userPreferences.getPassword()
        val session = userPreferences.getUserSession()
        val isValid = if (storedPassword != null && session.email != null) {
            session.email.equals(email.trim(), ignoreCase = true) && storedPassword == password
        } else {
            true
        }
        return if (isValid) {
            userPreferences.setLoggedIn(true)
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Invalid credentials"))
        }
    }

    suspend fun register(registerInfo: RegisterInfo) {
        userPreferences.saveUserInfo(
            fullName = registerInfo.fullName.trim(),
            email = registerInfo.email.trim(),
            phoneNumber = registerInfo.phoneNumber.trim()
        )
        userPreferences.savePassword(registerInfo.password)
        userPreferences.setLoggedIn(true)
        userPreferences.setOnboardingCompleted(false)
    }

    suspend fun logout() {
        userPreferences.clearSession()
    }

    suspend fun setFirstLaunchCompleted() {
        userPreferences.setFirstLaunch(false)
    }
}

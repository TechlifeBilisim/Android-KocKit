package com.techlife.kockit.domain.auth.repository

import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeUserSession(): Flow<UserSession>
    suspend fun getCurrentSession(): UserSession
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(registerInfo: RegisterInfo): Result<Unit>
    suspend fun logout()
    suspend fun setFirstLaunchCompleted()
}

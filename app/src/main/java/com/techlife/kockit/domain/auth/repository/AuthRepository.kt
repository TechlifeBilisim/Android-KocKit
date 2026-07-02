package com.techlife.kockit.domain.auth.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeUserSession(): Flow<UserSession>
    suspend fun getCurrentSession(): UserSession
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(registerInfo: RegisterInfo): ApiResult<RegisterResult>
    suspend fun logout()
    suspend fun setFirstLaunchCompleted()
}

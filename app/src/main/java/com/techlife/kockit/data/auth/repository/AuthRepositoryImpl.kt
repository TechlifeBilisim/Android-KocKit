package com.techlife.kockit.data.auth.repository

import com.techlife.kockit.data.auth.local.AuthLocalDataSource
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {

    override fun observeUserSession(): Flow<UserSession> =
        authLocalDataSource.observeUserSession()

    override suspend fun getCurrentSession(): UserSession =
        authLocalDataSource.getUserSession()

    override suspend fun login(email: String, password: String): Result<Unit> =
        authLocalDataSource.login(email, password)

    override suspend fun register(registerInfo: RegisterInfo): Result<Unit> = runCatching {
        authLocalDataSource.register(registerInfo)
    }

    override suspend fun logout() {
        authLocalDataSource.logout()
    }

    override suspend fun setFirstLaunchCompleted() {
        authLocalDataSource.setFirstLaunchCompleted()
    }
}

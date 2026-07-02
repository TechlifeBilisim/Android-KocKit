package com.techlife.kockit.data.auth.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.auth.local.AuthLocalDataSource
import com.techlife.kockit.data.remote.datasource.AuthRemoteDataSource
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override fun observeUserSession(): Flow<UserSession> =
        authLocalDataSource.observeUserSession()

    override suspend fun getCurrentSession(): UserSession =
        authLocalDataSource.getUserSession()

    override suspend fun login(email: String, password: String): Result<Unit> =
        authLocalDataSource.login(email, password)

    override suspend fun register(registerInfo: RegisterInfo): ApiResult<RegisterResult> {
        return when (val result = authRemoteDataSource.register(registerInfo)) {
            is ApiResult.Success -> {
                authLocalDataSource.persistRegistration(registerInfo, result.data)
                result
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun logout() {
        authLocalDataSource.logout()
    }

    override suspend fun setFirstLaunchCompleted() {
        authLocalDataSource.setFirstLaunchCompleted()
    }
}

package com.techlife.kockit.data.auth.local

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.core.util.normalizeTurkishPhone
import com.techlife.kockit.domain.auth.model.LoginResult
import com.techlife.kockit.domain.auth.model.RegisterInfo
import com.techlife.kockit.domain.auth.model.RegisterResult
import com.techlife.kockit.domain.auth.model.UserSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferences
) {
    fun observeUserSession(): Flow<UserSession> = userPreferences.userSessionFlow

    suspend fun getUserSession(): UserSession = userPreferences.getUserSession()

    suspend fun persistLogin(result: LoginResult, password: String? = null) {
        result.accessToken?.let { token ->
            userPreferences.saveAuthTokens(token, result.refreshToken)
        }
        userPreferences.saveUserInfo(
            fullName = result.fullName,
            email = result.email,
            phoneNumber = result.phone,
            kullaniciId = result.userId
        )
        password?.let { userPreferences.savePassword(it) }
        userPreferences.setLoggedIn(true)
    }

    suspend fun getRefreshToken(): String? = userPreferences.getRefreshToken()

    suspend fun getAccessToken(): String? = userPreferences.getAccessToken()

    suspend fun getKullaniciId(): String? = userPreferences.getKullaniciId()

    suspend fun persistRegistration(registerInfo: RegisterInfo, result: RegisterResult) {
        userPreferences.saveAuthTokens(result.accessToken, result.refreshToken)
        val phoneNumber = result.phone?.takeIf { it.isNotBlank() }
            ?: normalizeTurkishPhone(registerInfo.phone).takeIf { it.isNotBlank() }
        userPreferences.saveUserInfo(
            fullName = registerInfo.fullName.trim(),
            email = result.email ?: registerInfo.email.trim(),
            phoneNumber = phoneNumber,
            kullaniciId = result.userId
        )
        userPreferences.savePassword(registerInfo.password)
        userPreferences.setLoggedIn(true)
        userPreferences.setOnboardingCompleted(false)
        // Yeni kayıt olan kullanıcı yeniden açılışta tekrar giriş yapmak zorunda kalmasın.
        userPreferences.setRememberMe(true, phoneNumber)
    }

    suspend fun setRememberMe(remember: Boolean, phone: String?) =
        userPreferences.setRememberMe(remember, phone)

    suspend fun isRemembered(): Boolean = userPreferences.getRememberMe()

    suspend fun getRememberedPhone(): String? = userPreferences.getRememberedPhone()

    suspend fun logout() {
        userPreferences.clearSession()
    }

    suspend fun setFirstLaunchCompleted() {
        userPreferences.setFirstLaunch(false)
    }
}

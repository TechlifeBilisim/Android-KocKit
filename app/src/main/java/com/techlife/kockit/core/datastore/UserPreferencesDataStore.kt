package com.techlife.kockit.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.techlife.kockit.domain.auth.model.UserSession
import com.techlife.kockit.domain.placement.model.PlacementProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferences {

    override val userSessionFlow: Flow<UserSession> = dataStore.data.map { prefs ->
        prefs.toUserSession()
    }

    override val placementProgressFlow: Flow<PlacementProgress> = dataStore.data.map { prefs ->
        prefs.toPlacementProgress()
    }

    override val accessTokenFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[Keys.ACCESS_TOKEN]
    }

    override suspend fun getUserSession(): UserSession = dataStore.data.first().toUserSession()

    override suspend fun getAccessToken(): String? = dataStore.data.first()[Keys.ACCESS_TOKEN]

    override suspend fun getRefreshToken(): String? = dataStore.data.first()[Keys.REFRESH_TOKEN]

    override suspend fun saveAuthTokens(accessToken: String, refreshToken: String?) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            if (refreshToken != null) {
                prefs[Keys.REFRESH_TOKEN] = refreshToken
            } else {
                prefs.remove(Keys.REFRESH_TOKEN)
            }
        }
    }

    override suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        dataStore.edit { it[Keys.IS_FIRST_LAUNCH] = isFirstLaunch }
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { it[Keys.IS_LOGGED_IN] = isLoggedIn }
    }

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { it[Keys.IS_ONBOARDING_COMPLETED] = isCompleted }
    }

    override suspend fun saveUserInfo(
        fullName: String?,
        email: String?,
        phoneNumber: String?
    ) {
        dataStore.edit { prefs ->
            fullName?.let { prefs[Keys.FULL_NAME] = it }
            email?.let { prefs[Keys.EMAIL] = it }
            phoneNumber?.let { prefs[Keys.PHONE_NUMBER] = it }
        }
    }

    override suspend fun saveOnboardingSelections(
        examGoal: String?,
        university: String?,
        department: String?
    ) {
        dataStore.edit { prefs ->
            examGoal?.let { prefs[Keys.SELECTED_EXAM_GOAL] = it }
            university?.let { prefs[Keys.SELECTED_UNIVERSITY] = it }
            department?.let { prefs[Keys.SELECTED_DEPARTMENT] = it }
        }
    }

    override suspend fun savePassword(password: String) {
        dataStore.edit { it[Keys.PASSWORD] = password }
    }

    override suspend fun getPassword(): String? =
        dataStore.data.first()[Keys.PASSWORD]

    override suspend fun setPlacementSectionCompleted(sectionKey: String, completed: Boolean) {
        dataStore.edit { prefs ->
            when (sectionKey) {
                PlacementProgress.SECTION_GENERAL_ABILITY ->
                    prefs[Keys.PLACEMENT_ABILITY_COMPLETED] = completed
                PlacementProgress.SECTION_GENERAL_CULTURE ->
                    prefs[Keys.PLACEMENT_CULTURE_COMPLETED] = completed
            }
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
            prefs[Keys.IS_ONBOARDING_COMPLETED] = false
            prefs.remove(Keys.FULL_NAME)
            prefs.remove(Keys.EMAIL)
            prefs.remove(Keys.PHONE_NUMBER)
            prefs.remove(Keys.SELECTED_EXAM_GOAL)
            prefs.remove(Keys.SELECTED_UNIVERSITY)
            prefs.remove(Keys.SELECTED_DEPARTMENT)
            prefs.remove(Keys.PASSWORD)
            prefs.remove(Keys.ACCESS_TOKEN)
            prefs.remove(Keys.REFRESH_TOKEN)
            prefs.remove(Keys.PLACEMENT_ABILITY_COMPLETED)
            prefs.remove(Keys.PLACEMENT_CULTURE_COMPLETED)
        }
    }

    private fun Preferences.toPlacementProgress(): PlacementProgress = PlacementProgress(
        isGeneralAbilityCompleted = this[Keys.PLACEMENT_ABILITY_COMPLETED] ?: false,
        isGeneralCultureCompleted = this[Keys.PLACEMENT_CULTURE_COMPLETED] ?: false
    )

    private fun Preferences.toUserSession(): UserSession = UserSession(
        isFirstLaunch = this[Keys.IS_FIRST_LAUNCH] ?: true,
        isLoggedIn = this[Keys.IS_LOGGED_IN] ?: false,
        isOnboardingCompleted = this[Keys.IS_ONBOARDING_COMPLETED] ?: false,
        fullName = this[Keys.FULL_NAME],
        email = this[Keys.EMAIL],
        phoneNumber = this[Keys.PHONE_NUMBER],
        selectedExamGoal = this[Keys.SELECTED_EXAM_GOAL],
        selectedUniversity = this[Keys.SELECTED_UNIVERSITY],
        selectedDepartment = this[Keys.SELECTED_DEPARTMENT]
    )

    private object Keys {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val FULL_NAME = stringPreferencesKey("full_name")
        val EMAIL = stringPreferencesKey("email")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
        val SELECTED_EXAM_GOAL = stringPreferencesKey("selected_exam_goal")
        val SELECTED_UNIVERSITY = stringPreferencesKey("selected_university")
        val SELECTED_DEPARTMENT = stringPreferencesKey("selected_department")
        val PASSWORD = stringPreferencesKey("password")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val PLACEMENT_ABILITY_COMPLETED = booleanPreferencesKey("placement_ability_completed")
        val PLACEMENT_CULTURE_COMPLETED = booleanPreferencesKey("placement_culture_completed")
    }
}

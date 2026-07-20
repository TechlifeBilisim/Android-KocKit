package com.techlife.kockit.feature.profile

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.core.util.ProfileImageCodec
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.kullanici.model.KullaniciProfile
import com.techlife.kockit.domain.location.usecase.GetDistrictsUseCase
import com.techlife.kockit.domain.location.usecase.GetProvincesUseCase
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import com.techlife.kockit.domain.onboarding.model.Gender
import com.techlife.kockit.feature.profilegoals.ProfileGoalOptions

internal suspend fun resolveLocationLabel(
    ilId: Int?,
    ilceId: Int?,
    getProvincesUseCase: GetProvincesUseCase,
    getDistrictsUseCase: GetDistrictsUseCase
): String {
    if (ilId == null) return "-"
    val provinces = when (val result = getProvincesUseCase()) {
        is ApiResult.Success -> result.data
        is ApiResult.Error -> return "-"
    }
    val provinceName = provinces.find { it.id == ilId }?.name ?: return "-"
    if (ilceId == null) return provinceName
    val districts = when (val result = getDistrictsUseCase(ilId)) {
        is ApiResult.Success -> result.data
        is ApiResult.Error -> return provinceName
    }
    val districtName = districts.find { it.id == ilceId }?.name
    return if (districtName.isNullOrBlank()) provinceName else "$provinceName / $districtName"
}

internal fun KullaniciProfile.fullNameOrEmpty(): String =
    listOfNotNull(ad?.trim()?.takeIf { it.isNotBlank() }, soyad?.trim()?.takeIf { it.isNotBlank() })
        .joinToString(" ")

internal fun genderFromId(cinsiyetId: Int?): Gender? =
    cinsiyetId?.let { id -> Gender.entries.find { it.apiId == id } }

internal suspend fun syncSessionFromProfile(
    userPreferences: UserPreferences,
    profile: KullaniciProfile
) {
    userPreferences.saveUserInfo(
        fullName = profile.fullNameOrEmpty().ifBlank { null },
        email = profile.eposta,
        phoneNumber = profile.cepTelefon,
        kullaniciId = profile.kullaniciId,
        profileImage = ProfileImageCodec.sanitizeProfileImage(profile.resim)
    )
}

internal fun Ogrenci.toProfileUiState(location: String): ProfileUiState {
    val profile = kullanici
    val fullName = profile?.fullNameOrEmpty().orEmpty()
    val weeklyHours = haftalikCalismaSure?.takeIf { it > 0 }?.let { "$it Saat" } ?: "-"
    val dailyHours = gunlukOturumSure?.takeIf { it > 0 }?.let { "$it Saat" } ?: "-"
    val puanTurLabel = hazirlikPuanTurId
        ?.takeIf { it in 1..5 }
        ?.let { id -> ProfileGoalOptions.puanTurOptions.find { it.id == id.toString() }?.label }
        ?: "-"
    return ProfileUiState(
        isLoading = false,
        fullName = fullName,
        rumuz = profile?.rumuz.orEmpty(),
        email = profile?.eposta.orEmpty(),
        phone = profile?.cepTelefon.orEmpty(),
        location = location,
        school = okulAd?.takeIf { it.isNotBlank() } ?: "-",
        profileImage = ProfileImageCodec.sanitizeProfileImage(profile?.resim),
        grade = sinifDuzeyId?.takeIf { it > 0 }?.let { "$it. Sınıf" } ?: "-",
        examType = puanTurLabel,
        levelLabel = ogrenciHedef?.takeIf { it.isNotBlank() } ?: "Başarılı",
        weeklyStudyHours = weeklyHours,
        weeklyStudyProgress = if (haftalikCalismaSure != null && haftalikCalismaSure > 0) 0.7f else 0f,
        weeklyStudyPercent = if (haftalikCalismaSure != null && haftalikCalismaSure > 0) "%70" else "-",
        dailyStudyHours = dailyHours
    )
}

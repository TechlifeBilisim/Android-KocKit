package com.techlife.kockit.feature.editprofile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.core.util.ProfileImageCodec
import com.techlife.kockit.core.util.splitPersonName
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.auth.usecase.ObserveUserSessionUseCase
import com.techlife.kockit.domain.kullanici.model.UpdateKullaniciProfile
import com.techlife.kockit.domain.kullanici.usecase.UpdateKullaniciProfileUseCase
import com.techlife.kockit.domain.location.model.District
import com.techlife.kockit.domain.location.model.Province
import com.techlife.kockit.domain.location.usecase.GetDistrictsUseCase
import com.techlife.kockit.domain.location.usecase.GetProvincesUseCase
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import com.techlife.kockit.domain.onboarding.model.Gender
import com.techlife.kockit.feature.profile.genderFromId
import com.techlife.kockit.feature.profile.syncSessionFromProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEncodingImage: Boolean = false,
    val isProvincesLoading: Boolean = false,
    val isDistrictsLoading: Boolean = false,
    val ad: String = "",
    val soyad: String = "",
    val rumuz: String = "",
    val eposta: String = "",
    val selectedGender: Gender? = null,
    val provinces: List<Province> = emptyList(),
    val districts: List<District> = emptyList(),
    val selectedProvinceId: Int? = null,
    val selectedProvinceName: String? = null,
    val selectedDistrictId: Int? = null,
    val selectedDistrictName: String? = null,
    val profileImagePreview: String? = null,
    val profileImagePayload: String? = null,
    val selectedTemaId: Int = 1,
    val adError: String? = null,
    val soyadError: String? = null,
    val rumuzError: String? = null,
    val epostaError: String? = null,
    val genderError: String? = null,
    val provinceError: String? = null,
    val districtError: String? = null,
    val provincesError: String? = null,
    val districtsError: String? = null,
    val loadError: String? = null
)

sealed interface EditProfileEvent {
    data class AdChanged(val value: String) : EditProfileEvent
    data class SoyadChanged(val value: String) : EditProfileEvent
    data class RumuzChanged(val value: String) : EditProfileEvent
    data class EpostaChanged(val value: String) : EditProfileEvent
    data class GenderSelected(val gender: Gender) : EditProfileEvent
    data class ProvinceSelected(val provinceId: Int, val name: String) : EditProfileEvent
    data class DistrictSelected(val districtId: Int, val name: String) : EditProfileEvent
    data class TemaSelected(val temaId: Int) : EditProfileEvent
    data class ImageSelected(val uri: Uri) : EditProfileEvent
    data object SaveClick : EditProfileEvent
}

sealed interface EditProfileEffect {
    data object Saved : EditProfileEffect
    data class ShowMessage(val message: String) : EditProfileEffect
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getKullaniciIdUseCase: GetKullaniciIdUseCase,
    private val getOgrenciUseCase: GetOgrenciUseCase,
    private val observeUserSessionUseCase: ObserveUserSessionUseCase,
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val updateKullaniciProfileUseCase: UpdateKullaniciProfileUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EditProfileEffect>()
    val effect: SharedFlow<EditProfileEffect> = _effect.asSharedFlow()

    fun loadProfile() {
        loadInitial()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.AdChanged -> _uiState.update {
                it.copy(ad = event.value, adError = null)
            }
            is EditProfileEvent.SoyadChanged -> _uiState.update {
                it.copy(soyad = event.value, soyadError = null)
            }
            is EditProfileEvent.RumuzChanged -> _uiState.update {
                it.copy(rumuz = event.value, rumuzError = null)
            }
            is EditProfileEvent.EpostaChanged -> _uiState.update {
                it.copy(eposta = event.value, epostaError = null)
            }
            is EditProfileEvent.GenderSelected -> _uiState.update {
                it.copy(selectedGender = event.gender, genderError = null)
            }
            is EditProfileEvent.ProvinceSelected -> {
                _uiState.update {
                    it.copy(
                        selectedProvinceId = event.provinceId,
                        selectedProvinceName = event.name,
                        selectedDistrictId = null,
                        selectedDistrictName = null,
                        districts = emptyList(),
                        provinceError = null,
                        districtError = null,
                        districtsError = null,
                        isDistrictsLoading = true
                    )
                }
                loadDistricts(event.provinceId)
            }
            is EditProfileEvent.DistrictSelected -> _uiState.update {
                it.copy(
                    selectedDistrictId = event.districtId,
                    selectedDistrictName = event.name,
                    districtError = null
                )
            }
            is EditProfileEvent.TemaSelected -> _uiState.update {
                it.copy(selectedTemaId = event.temaId)
            }
            is EditProfileEvent.ImageSelected -> encodeSelectedImage(event.uri)
            EditProfileEvent.SaveClick -> save()
        }
    }

    private fun loadInitial() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isProvincesLoading = true,
                    isDistrictsLoading = false,
                    loadError = null,
                    provincesError = null,
                    districtsError = null
                )
            }
            val session = observeUserSessionUseCase().first()
            val kullaniciId = getKullaniciIdUseCase() ?: session.kullaniciId

            val provinces = when (val result = getProvincesUseCase()) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    _uiState.update { it.copy(provincesError = result.message) }
                    emptyList()
                }
            }
            _uiState.update {
                it.copy(
                    provinces = provinces,
                    isProvincesLoading = false
                )
            }

            val profile = when (
                val result = kullaniciId
                    ?.takeIf { it.isNotBlank() }
                    ?.let { getOgrenciUseCase(it) }
            ) {
                is ApiResult.Success -> {
                    result.data.kullanici?.also { syncSessionFromProfile(userPreferences, it) }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(loadError = result.message) }
                    null
                }
                null -> null
            }

            var districts = emptyList<District>()
            var provinceName: String? = null
            var districtName: String? = null
            profile?.ilId?.let { ilId ->
                provinceName = provinces.find { it.id == ilId }?.name
                _uiState.update { it.copy(isDistrictsLoading = true) }
                when (val districtResult = getDistrictsUseCase(ilId)) {
                    is ApiResult.Success -> {
                        districts = districtResult.data
                        districtName = districtResult.data.find { it.id == profile.ilceId }?.name
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(districtsError = districtResult.message) }
                    }
                }
            }

            val fallbackName = splitPersonName(session.fullName.orEmpty())
            val profileImage = ProfileImageCodec.sanitizeProfileImage(profile?.resim)
                ?: ProfileImageCodec.sanitizeProfileImage(session.profileImage)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isDistrictsLoading = false,
                    ad = profile?.ad?.takeIf { name -> name.isNotBlank() } ?: fallbackName.first,
                    soyad = profile?.soyad?.takeIf { name -> name.isNotBlank() } ?: fallbackName.second,
                    rumuz = profile?.rumuz?.takeIf { name -> name.isNotBlank() }
                        ?: session.fullName.orEmpty().substringBefore(' ').ifBlank { fallbackName.first },
                    eposta = profile?.eposta?.takeIf { mail -> mail.isNotBlank() }
                        ?: session.email.orEmpty(),
                    selectedGender = genderFromId(profile?.cinsiyetId),
                    districts = districts,
                    selectedProvinceId = profile?.ilId,
                    selectedProvinceName = provinceName,
                    selectedDistrictId = profile?.ilceId,
                    selectedDistrictName = districtName,
                    profileImagePreview = profileImage,
                    profileImagePayload = profileImage,
                    selectedTemaId = profile?.uygulamaTemaId ?: DEFAULT_TEMA_ID
                )
            }
        }
    }

    private fun encodeSelectedImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isEncodingImage = true) }
            val encoded = withContext(Dispatchers.IO) {
                ProfileImageCodec.encodeUriToBase64(context, uri)
            }
            if (encoded == null) {
                _uiState.update { it.copy(isEncodingImage = false) }
                _effect.emit(EditProfileEffect.ShowMessage("Fotoğraf okunamadı."))
                return@launch
            }
            _uiState.update {
                it.copy(
                    isEncodingImage = false,
                    profileImagePreview = uri.toString(),
                    profileImagePayload = encoded
                )
            }
        }
    }

    private fun loadDistricts(provinceId: Int, preserveSelection: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDistrictsLoading = true, districtsError = null) }
            when (val result = getDistrictsUseCase(provinceId)) {
                is ApiResult.Success -> _uiState.update { state ->
                    state.copy(
                        districts = result.data,
                        isDistrictsLoading = false,
                        districtsError = null,
                        selectedDistrictId = state.selectedDistrictId?.takeIf { preserveSelection },
                        selectedDistrictName = state.selectedDistrictName?.takeIf { preserveSelection }
                    )
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        districts = emptyList(),
                        isDistrictsLoading = false,
                        districtsError = result.message
                    )
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        val adError = if (state.ad.isBlank()) "Ad gerekli" else null
        val soyadError = if (state.soyad.isBlank()) "Soyad gerekli" else null
        val rumuzError = if (state.rumuz.isBlank()) "Rumuz gerekli" else null
        val epostaError = when {
            state.eposta.isBlank() -> "E-posta gerekli"
            '@' !in state.eposta -> "Geçerli bir e-posta girin"
            else -> null
        }
        val genderError = if (state.selectedGender == null) "Cinsiyet seçimi gerekli" else null
        val provinceError = if (state.selectedProvinceId == null) "İl seçimi gerekli" else null
        val districtError = if (state.selectedDistrictId == null) "İlçe seçimi gerekli" else null
        if (listOf(
                adError,
                soyadError,
                rumuzError,
                epostaError,
                genderError,
                provinceError,
                districtError
            ).any { it != null }
        ) {
            _uiState.update {
                it.copy(
                    adError = adError,
                    soyadError = soyadError,
                    rumuzError = rumuzError,
                    epostaError = epostaError,
                    genderError = genderError,
                    provinceError = provinceError,
                    districtError = districtError
                )
            }
            return
        }

        val gender = state.selectedGender ?: return
        val provinceId = state.selectedProvinceId ?: return
        val districtId = state.selectedDistrictId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (
                val result = updateKullaniciProfileUseCase(
                    UpdateKullaniciProfile(
                        ad = state.ad.trim(),
                        soyad = state.soyad.trim(),
                        rumuz = state.rumuz.trim(),
                        eposta = state.eposta.trim(),
                        cinsiyetId = gender.apiId,
                        ilId = provinceId,
                        ilceId = districtId,
                        resim = state.profileImagePayload,
                        uygulamaTemaId = state.selectedTemaId
                    )
                )
            ) {
                is ApiResult.Success -> {
                    val profile = result.data
                    syncSessionFromProfile(userPreferences, profile)
                    val savedImage = ProfileImageCodec.sanitizeProfileImage(profile.resim)
                        ?: state.profileImagePayload
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            profileImagePreview = savedImage,
                            profileImagePayload = savedImage
                        )
                    }
                    _effect.emit(EditProfileEffect.ShowMessage("Profiliniz güncellendi."))
                    _effect.emit(EditProfileEffect.Saved)
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.emit(EditProfileEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private companion object {
        const val DEFAULT_TEMA_ID = 1
    }
}

object EditProfileTemaOptions {
    val options = listOf(
        1 to "Varsayılan",
        2 to "Koyu"
    )
}

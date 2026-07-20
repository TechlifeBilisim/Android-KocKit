package com.techlife.kockit.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.auth.usecase.GetKullaniciIdUseCase
import com.techlife.kockit.domain.location.usecase.GetDistrictsUseCase
import com.techlife.kockit.domain.location.usecase.GetProvincesUseCase
import com.techlife.kockit.domain.ogrenci.usecase.GetOgrenciUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getKullaniciIdUseCase: GetKullaniciIdUseCase,
    private val getOgrenciUseCase: GetOgrenciUseCase,
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getDistrictsUseCase: GetDistrictsUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun refresh() {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val kullaniciId = getKullaniciIdUseCase()
            if (kullaniciId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Oturum bulunamadı.")
                }
                return@launch
            }
            when (val result = getOgrenciUseCase(kullaniciId)) {
                is ApiResult.Success -> {
                    val ogrenci = result.data
                    val profile = ogrenci.kullanici
                    profile?.let { syncSessionFromProfile(userPreferences, it) }
                    val location = resolveLocationLabel(
                        ilId = profile?.ilId,
                        ilceId = profile?.ilceId,
                        getProvincesUseCase = getProvincesUseCase,
                        getDistrictsUseCase = getDistrictsUseCase
                    )
                    _uiState.update { ogrenci.toProfileUiState(location) }
                }
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }
}

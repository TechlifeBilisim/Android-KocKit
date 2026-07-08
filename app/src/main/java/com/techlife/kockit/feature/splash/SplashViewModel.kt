package com.techlife.kockit.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.auth.usecase.HasActiveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val hasActiveSessionUseCase: HasActiveSessionUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val hasSession = hasActiveSessionUseCase()
            delay(SPLASH_DURATION_MS)
            if (hasSession) {
                emit(SplashEffect.NavigateToMain)
            } else {
                emit(SplashEffect.NavigateToLogin)
            }
        }
    }

    private fun emit(effect: SplashEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val SPLASH_DURATION_MS = 500L
    }
}

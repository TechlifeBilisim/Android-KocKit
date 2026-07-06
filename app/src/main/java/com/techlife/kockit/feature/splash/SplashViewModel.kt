package com.techlife.kockit.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.auth.usecase.CheckSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val session = checkSessionUseCase()
            if (session.isLoggedIn) {
                emit(SplashEffect.NavigateToMain)
            }
        }
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.StartClicked -> emit(SplashEffect.NavigateToRegister)
            SplashEvent.LoginClicked -> emit(SplashEffect.NavigateToLogin)
        }
    }

    private fun emit(effect: SplashEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}

package com.techlife.kockit.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

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

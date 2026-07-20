package com.techlife.kockit.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.auth.model.StartupDestination
import com.techlife.kockit.domain.auth.usecase.GetStartupDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getStartupDestinationUseCase: GetStartupDestinationUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val destination = getStartupDestinationUseCase()
            delay(SPLASH_DURATION_MS)
            emit(
                when (destination) {
                    StartupDestination.Login -> SplashEffect.NavigateToLogin
                    StartupDestination.GoalSetup -> SplashEffect.NavigateToGoalSetup
                    StartupDestination.Main -> SplashEffect.NavigateToMain
                }
            )
        }
    }

    private fun emit(effect: SplashEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    private companion object {
        const val SPLASH_DURATION_MS = 500L
    }
}

package com.techlife.kockit.feature.splash

sealed interface SplashEvent {
    data object StartClicked : SplashEvent
    data object LoginClicked : SplashEvent
}

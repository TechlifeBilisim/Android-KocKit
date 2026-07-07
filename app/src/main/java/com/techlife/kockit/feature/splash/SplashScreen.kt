package com.techlife.kockit.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashEffect.NavigateToLogin -> onNavigateToLogin()
                SplashEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    SplashScreenContent()
}

@Composable
internal fun SplashScreenContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.img_splash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    KocKitTheme {
        SplashScreenContent()
    }
}

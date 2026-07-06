package com.techlife.kockit.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitSplashActionButton
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashEffect.NavigateToLogin -> onNavigateToLogin()
                SplashEffect.NavigateToRegister -> onNavigateToRegister()
                SplashEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    SplashScreenContent(
        onStartClicked = { viewModel.onEvent(SplashEvent.StartClicked) },
        onLoginClicked = { viewModel.onEvent(SplashEvent.LoginClicked) }
    )
}

@Composable
internal fun SplashScreenContent(
    onStartClicked: () -> Unit,
    onLoginClicked: () -> Unit
) {
    val colors = KocKitTheme.extraColors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Image(
            painter = painterResource(R.drawable.img_splash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KocKitSplashActionButton(
                text = "Başlayalım",
                onClick = onStartClicked
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitText(
                    text = "Zaten hesabın var mı? ",
                    fontSize = KocKitTextDefaults.fontSizeSubhead,
                    lineHeight = KocKitTextDefaults.lineHeightSubhead,
                    color = colors.textPrimary
                )
                KocKitSemiText(
                    text = "Giriş Yap",
                    modifier = Modifier.clickable { onLoginClicked() },
                    fontSize = KocKitTextDefaults.fontSizeSubhead,
                    lineHeight = KocKitTextDefaults.lineHeightSubhead,
                    color = colors.textPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    KocKitTheme {
        SplashScreenContent(
            onStartClicked = {},
            onLoginClicked = {}
        )
    }
}

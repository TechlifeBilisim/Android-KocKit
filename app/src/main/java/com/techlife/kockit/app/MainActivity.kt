package com.techlife.kockit.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val cream = Color.parseColor("#FAF6EF")
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(cream, cream),
            navigationBarStyle = SystemBarStyle.light(cream, cream)
        )
        super.onCreate(savedInstanceState)
        setContent {
            KocKitTheme {
                AppNavGraph()
            }
        }
    }
}

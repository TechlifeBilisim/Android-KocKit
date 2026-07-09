package com.techlife.kockit.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.feature.map.components.ProvinceInfoDialog
import com.techlife.kockit.feature.map.components.TurkeyMapCanvas
import com.techlife.kockit.feature.map.model.TurkeyMapData
import com.techlife.kockit.feature.map.model.TurkeyProvince
import com.techlife.kockit.feature.map.parser.TurkeyMapParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TurkeyMapScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var mapData by remember { mutableStateOf<TurkeyMapData?>(null) }

    LaunchedEffect(context) {
        mapData = withContext(Dispatchers.Default) {
            runCatching { TurkeyMapParser.load(context, R.raw.turkiye_haritasi) }
                .getOrDefault(TurkeyMapData.EMPTY)
        }
    }

    TurkeyMapScreenContent(
        mapData = mapData,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun TurkeyMapScreenContent(
    mapData: TurkeyMapData?,
    onNavigateBack: () -> Unit
) {
    var selectedProvince by remember { mutableStateOf<TurkeyProvince?>(null) }
    var showProvinceDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        when {
            mapData == null -> {
                CircularProgressIndicator(
                    color = PastelGreen,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            mapData.provinces.isEmpty() -> {
                KocKitSemiText(
                    text = "Harita verisi yüklenemedi.",
                    color = TextSecondary,
                    fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                    lineHeight = KocKitTextDefaults.lineHeightBodyLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }
            else -> {
                TurkeyMapCanvas(
                    mapData = mapData,
                    modifier = Modifier.fillMaxSize(),
                    selectedPlateCode = selectedProvince?.plateCode,
                    onProvinceClick = { province ->
                        selectedProvince = province
                        showProvinceDialog = true
                    }
                )
            }
        }

        Box(modifier = Modifier.align(Alignment.TopStart)) {
            KocKitTopBar(onBackClick = onNavigateBack)
        }
    }

    ProvinceInfoDialog(
        province = if (showProvinceDialog) selectedProvince else null,
        onDismiss = { showProvinceDialog = false }
    )
}

package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KocKitTopBar(
    onBackClick: () -> Unit,
    title: String? = null
) {
    TopAppBar(
        title = {
            title?.let {
                KocKitSemiText(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            KocKitBackButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 4.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Preview(showBackground = true)
@Composable
private fun KocKitTopBarPreview() {
    KocKitTheme {
        Column {
            KocKitTopBar(
                onBackClick = {},
                title = "Screen Title"
            )
            KocKitTopBar(
                onBackClick = {}
            )
        }
    }
}

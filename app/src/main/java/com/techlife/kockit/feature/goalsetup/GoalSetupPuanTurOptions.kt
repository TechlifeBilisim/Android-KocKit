package com.techlife.kockit.feature.goalsetup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen

data class GoalSetupPuanTurOption(
    val id: Int,
    val label: String,
    val icon: ImageVector,
    val accentColor: Color
)

object GoalSetupPuanTurOptions {
    val options = listOf(
        GoalSetupPuanTurOption(
            id = PUAN_TUR_SOZEL,
            label = "Sözel",
            icon = Icons.Outlined.MenuBook,
            accentColor = LavenderAccent
        ),
        GoalSetupPuanTurOption(
            id = PUAN_TUR_SAYISAL,
            label = "Sayısal",
            icon = Icons.Outlined.Functions,
            accentColor = OrangeAccent
        ),
        GoalSetupPuanTurOption(
            id = PUAN_TUR_EA,
            label = "Eşit Ağırlık",
            icon = Icons.Outlined.Balance,
            accentColor = PastelGreen
        ),
        GoalSetupPuanTurOption(
            id = PUAN_TUR_TYT,
            label = "TYT",
            icon = Icons.Outlined.List,
            accentColor = OrangeAccent
        )
    )

    const val PUAN_TUR_TYT = 1
    const val PUAN_TUR_SAYISAL = 2
    const val PUAN_TUR_SOZEL = 3
    const val PUAN_TUR_EA = 4
}

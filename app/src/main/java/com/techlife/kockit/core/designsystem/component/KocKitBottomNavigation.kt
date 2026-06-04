package com.techlife.kockit.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.scale
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White
import com.techlife.kockit.feature.main.MainTab

@Composable
fun KocKitBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = White
    ) {
        MainTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            val iconColor by animateColorAsState(
                targetValue = if (selected) OrangeAccent else TextSecondary,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "navIconColor"
            )
            val labelColor by animateColorAsState(
                targetValue = if (selected) OrangeAccent else TextSecondary,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "navLabelColor"
            )
            val iconScale by animateFloatAsState(
                targetValue = if (selected) 1.1f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "navIconScale"
            )

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.label,
                        modifier = Modifier
                            .size(24.dp)
                            .scale(iconScale),
                        tint = iconColor
                    )
                },
                label = {
                    KocKitText(
                        text = tab.label,
                        fontSize = 11.sp,
                        lineHeight = KocKitTextDefaults.lineHeightSmall,
                        color = labelColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangeAccent,
                    selectedTextColor = OrangeAccent,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = OrangeAccent.copy(alpha = 0.14f)
                )
            )
        }
    }
}

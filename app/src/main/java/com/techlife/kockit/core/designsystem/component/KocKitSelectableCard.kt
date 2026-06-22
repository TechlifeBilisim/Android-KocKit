package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.techlife.kockit.core.designsystem.theme.CardShape
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PrimaryTeal
import com.techlife.kockit.core.designsystem.theme.White

@Composable
fun KocKitSelectableCard(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    leadingIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardShape,
    showSelectionBorder: Boolean = true,
    showRadioButton: Boolean = true,
    showExpandArrow: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected && showSelectionBorder) {
            BorderStroke(2.dp, White.copy(alpha = 0.85f))
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected && showSelectionBorder) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(color = White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        leadingIcon,
                        contentDescription = null,
                        tint = backgroundColor
                    )
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KocKitBoldText(text = title, color = White, fontSize = 26.sp)
                        if (showExpandArrow) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = White
                            )
                        }
                    }
                    KocKitText(text = subtitle, color = White.copy(alpha = 0.85f), fontSize = 16.sp)
                }
            }
            if (showRadioButton) {
                KocKitExamRadioButton(isSelected = isSelected)
            }
        }
    }
}

@Composable
fun KocKitExamRadioButton(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .then(
                if (isSelected) {
                    Modifier.background(White, CircleShape)
                } else {
                    Modifier.border(2.dp, White.copy(alpha = 0.9f), CircleShape)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color(0xFF5BB199),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitSelectableCardSelectedPreview() {
    KocKitTheme {
        KocKitSelectableCard(
            title = "Matematik",
            subtitle = "Problemler",
            backgroundColor = PrimaryTeal,
            leadingIcon = Icons.Filled.Check,
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitSelectableCardUnselectedPreview() {
    KocKitTheme {
        KocKitSelectableCard(
            title = "Türkçe",
            subtitle = "Paragraf",
            backgroundColor = OrangeAccent,
            leadingIcon = Icons.Filled.Check,
            isSelected = false,
            onClick = {}
        )
    }
}

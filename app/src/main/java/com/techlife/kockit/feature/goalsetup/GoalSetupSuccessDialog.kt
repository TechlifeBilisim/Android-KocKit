package com.techlife.kockit.feature.goalsetup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GoalSetupSuccessDialog(
    onDismiss: () -> Unit,
    onGoToPlacement: () -> Unit,
    onGoToHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape = RoundedCornerShape(24.dp),
            color = White,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }

                GoalSetupSuccessIllustration()

                Spacer(modifier = Modifier.height(12.dp))

                KocKitBoldText(
                    text = "Kayıt Oluşturuldu!",
                    color = TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeTitle,
                    lineHeight = KocKitTextDefaults.lineHeightTitle,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                KocKitText(
                    text = "Deneme sınavlarını olmayı unutma. Hedeflerine ulaşmak için ilk adımı attın! 🎯",
                    color = TextSecondary,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                GoalSetupSuccessActionButton(
                    text = "Deneme Sınavına Geç",
                    icon = Icons.Filled.Description,
                    containerColor = PastelGreen,
                    contentColor = White,
                    borderColor = PastelGreen,
                    onClick = onGoToPlacement
                )

                Spacer(modifier = Modifier.height(10.dp))

                GoalSetupSuccessActionButton(
                    text = "Ana Sayfaya Geç",
                    icon = Icons.Filled.Home,
                    containerColor = White,
                    contentColor = TextPrimary,
                    borderColor = PastelGreen,
                    onClick = onGoToHome
                )
            }
        }
    }
}

@Composable
private fun GoalSetupSuccessIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        contentAlignment = Alignment.Center
    ) {
        KocKitText(
            text = "🎉",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 24.dp, y = 8.dp)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107),
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-28).dp, y = 12.dp)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107),
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-8).dp, y = (-20).dp)
        )

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterStart)
                .offset(x = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.MenuBook,
                contentDescription = null,
                tint = LavenderAccent,
                modifier = Modifier.size(52.dp)
            )
        }

        GoalSetupScallopedBadge(
            modifier = Modifier.size(88.dp)
        )

        Image(
            painter = painterResource(R.drawable.ic_placement_trophy_gold),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-12).dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun GoalSetupScallopedBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scallops = 12
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val innerRadius = radius * 0.82f
            val outerRadius = radius

            val path = Path()
            for (i in 0 until scallops * 2) {
                val angle = (i * PI / scallops).toFloat() - (PI / 2).toFloat()
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val x = center.x + cos(angle) * r
                val y = center.y + sin(angle) * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path = path, color = PastelGreen, style = Fill)
        }
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun GoalSetupSuccessActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickableNoRipple(onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        KocKitSemiText(
            text = text,
            color = contentColor,
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
    }
}

private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier =
    clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )

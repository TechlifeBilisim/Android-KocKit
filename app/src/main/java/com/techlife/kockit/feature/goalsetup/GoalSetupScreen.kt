package com.techlife.kockit.feature.goalsetup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.White
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoUniversite
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GoalSetupScreen(
    viewModel: GoalSetupViewModel,
    onNavigateToPlacement: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GoalSetupEffect.NavigateToPlacement -> onNavigateToPlacement()
                GoalSetupEffect.NavigateToMain -> onNavigateToMain()
                GoalSetupEffect.NavigateBack -> onNavigateBack()
                is GoalSetupEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    GoalSetupScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun GoalSetupScreenContent(
    uiState: GoalSetupUiState,
    onEvent: (GoalSetupEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        KocKitBackground(useFormBackgroundImage = true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                KocKitTopBar(onBackClick = { onEvent(GoalSetupEvent.BackClicked) })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GoalSetupForm(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
                GoalSetupFooter(
                    isLoading = uiState.isLoading,
                    onContinue = { onEvent(GoalSetupEvent.ContinueClicked) }
                )
            }
        }

        if (uiState.showSuccessDialog) {
            GoalSetupSuccessDialog(
                onDismiss = { onEvent(GoalSetupEvent.SuccessDialogDismissed) },
                onGoToPlacement = { onEvent(GoalSetupEvent.GoToPlacementClicked) },
                onGoToHome = { onEvent(GoalSetupEvent.GoToMainClicked) }
            )
        }
    }
}

@Composable
private fun GoalSetupForm(
    uiState: GoalSetupUiState,
    onEvent: (GoalSetupEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors

    GoalSetupOnlyTytCard(
        onlyTyt = uiState.onlyTyt,
        onToggle = { onEvent(GoalSetupEvent.OnlyTytToggled(it)) }
    )

    GoalSetupSectionTitle(text = "Puan Türü")
    GoalSetupPuanTurGrid(
        selectedPuanTurId = uiState.selectedPuanTurId,
        enabled = !uiState.onlyTyt,
        onSelect = { onEvent(GoalSetupEvent.PuanTurSelected(it)) }
    )
    uiState.puanTurError?.let { KocKitText(text = it, color = colors.coralAccent) }

    GoalSetupSectionTitle(text = "Sıralama Seç")
    KocKitTextField(
        value = formatSiralamaDisplay(uiState.siralamaInput),
        onValueChange = { onEvent(GoalSetupEvent.SiralamaChanged(it)) },
        placeholder = "50.000",
        leadingIconVector = Icons.Filled.KeyboardArrowUp,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        error = uiState.siralamaError
    )

    GoalSetupSectionTitle(text = "Üniversite Türü")
    GoalSetupUniversityTypeSwitch(
        selectedType = uiState.selectedUniversityType,
        onTypeSelected = { onEvent(GoalSetupEvent.UniversityTypeSelected(it)) }
    )
    uiState.universityTypeError?.let { KocKitText(text = it, color = colors.coralAccent) }

    KocKitDropdownField(
        label = "Bölüm Seç",
        options = uiState.bolumler.map { it.name },
        selectedOption = uiState.selectedBolumName,
        onOptionSelected = { name ->
            val bolum = uiState.bolumler.find { it.name == name } ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.BolumSelected(bolum.id, bolum.name))
        },
        error = uiState.bolumError ?: uiState.bolumlerError,
        searchable = true,
        searchPlaceholder = when {
            uiState.isBolumlerLoading -> "Bölümler yükleniyor..."
            else -> "Bölüm ara..."
        },
        leadingIcon = Icons.Filled.MenuBook,
        leadingIconTint = PastelGreen,
        leadingIconBackground = PastelGreen.copy(alpha = 0.12f)
    )
    KocKitDropdownField(
        label = "Üniversite Seç",
        options = uiState.universiteler.map { it.name },
        selectedOption = uiState.selectedUniversityName,
        onOptionSelected = { name ->
            val university = uiState.universiteler.find { it.name == name }
                ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.UniversitySelected(university.id, university.name))
        },
        error = uiState.universityError ?: uiState.universitelerError,
        searchable = true,
        searchPlaceholder = when {
            uiState.isUniversitelerLoading -> "Üniversiteler yükleniyor..."
            else -> "Üniversite ara..."
        },
        leadingIcon = Icons.Filled.School,
        leadingIconTint = PastelGreen,
        leadingIconBackground = PastelGreen.copy(alpha = 0.15f)
    )
}

@Composable
private fun GoalSetupFooter(
    isLoading: Boolean,
    onContinue: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GoalSetupStepIndicator(
            totalSteps = 4,
            currentStep = 0,
            modifier = Modifier.weight(1f)
        )
        GoalSetupContinueButton(
            isLoading = isLoading,
            onClick = onContinue,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
private fun GoalSetupStepIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentStep) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentStep) PastelGreen
                        else Color(0xFFD9DDE3)
                    )
            )
        }
    }
}

@Composable
private fun GoalSetupPuanTurGrid(
    selectedPuanTurId: Int?,
    enabled: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = GoalSetupPuanTurOptions.options
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowOptions.forEach { option ->
                    GoalSetupPuanTurCard(
                        option = option,
                        isSelected = selectedPuanTurId == option.id,
                        enabled = enabled,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowOptions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun GoalSetupPuanTurCard(
    option: GoalSetupPuanTurOption,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors
    val borderColor = when {
        isSelected -> option.accentColor
        else -> colors.borderLight.copy(alpha = 0.7f)
    }

    Column(
        modifier = modifier
            .clip(GoalSetupCardShape)
            .background(White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = GoalSetupCardShape
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 18.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = if (enabled) option.accentColor else option.accentColor.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
        KocKitSemiText(
            text = option.label,
            color = if (enabled) TextPrimary else TextPrimary.copy(alpha = 0.4f),
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun GoalSetupSectionTitle(text: String) {
    KocKitBoldText(
        text = text,
        color = TextPrimary,
        fontSize = KocKitTextDefaults.fontSizeTitle,
        lineHeight = KocKitTextDefaults.lineHeightTitle
    )
}

@Composable
private fun GoalSetupContinueButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = PastelGreen
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = White,
                    strokeWidth = 2.dp
                )
            } else {
                KocKitBoldText(
                    text = "Devam Et",
                    color = White,
                    fontSize = KocKitTextDefaults.fontSizeButton
                )
            }
        }
    }
}

private val GoalSetupCardShape = RoundedCornerShape(20.dp)

@Composable
private fun GoalSetupUniversityTypeSwitch(
    selectedType: UniversityType?,
    onTypeSelected: (UniversityType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors
    val options = listOf(
        GoalSetupUniversityTypeOption(null, "Tümü", iconPainter = null, iconVector = null, accentColor = OrangeAccent),
        GoalSetupUniversityTypeOption(
            UniversityType.DEVLET,
            "Devlet",
            iconPainter = null,
            iconVector = Icons.Filled.AccountBalance,
            accentColor = LavenderAccent
        ),
        GoalSetupUniversityTypeOption(
            UniversityType.VAKIF,
            "Vakıf",
            iconPainter = null,
            iconVector = Icons.Filled.Shield,
            accentColor = OrangeAccent
        ),
        GoalSetupUniversityTypeOption(
            UniversityType.OZEL,
            "Diğer",
            iconPainter = null,
            iconVector = Icons.Filled.Apps,
            accentColor = PastelGreen
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(GoalSetupCardShape)
            .background(White)
            .border(1.dp, colors.borderLight.copy(alpha = 0.7f), GoalSetupCardShape)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedType == option.type
            val backgroundColor = if (isSelected) OrangeAccent else Color.Transparent
            val contentColor = if (isSelected) White else TextPrimary
            val iconTint = if (isSelected) White else option.accentColor

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(backgroundColor)
                    .clickable { onTypeSelected(option.type) }
                    .padding(vertical = 12.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (option.type == null) {
                    Icon(
                        painter = painterResource(R.drawable.ic_goal_target),
                        contentDescription = null,
                        tint = if (isSelected) White else Color.Unspecified,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        imageVector = option.iconVector!!,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                KocKitSemiText(
                    text = option.label,
                    color = contentColor,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

private data class GoalSetupUniversityTypeOption(
    val type: UniversityType?,
    val label: String,
    val iconPainter: androidx.compose.ui.graphics.painter.Painter?,
    val iconVector: ImageVector?,
    val accentColor: Color
)

@Composable
private fun GoalSetupOnlyTytCard(
    onlyTyt: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(GoalSetupCardShape)
            .background(White)
            .border(1.dp, colors.borderLight.copy(alpha = 0.7f), GoalSetupCardShape)
            .clickable { onToggle(!onlyTyt) }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KocKitSemiText(
            text = "Sadece TYT'ye girmek istiyorum.",
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        GoalSetupPillSwitch(
            checked = onlyTyt,
            onCheckedChange = onToggle,
            activeColor = LavenderAccent
        )
    }
}

@Composable
private fun GoalSetupPillSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = LavenderAccent
) {
    val trackWidth = 52.dp
    val trackHeight = 30.dp
    val thumbSize = 24.dp
    val trackColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (checked) activeColor else Color(0xFFD9DDE3),
        label = "onlyTytTrack"
    )
    val thumbOffset by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - 3.dp else 3.dp,
        label = "onlyTytThumb"
    )

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(White)
        )
    }
}

private fun formatSiralamaDisplay(rawDigits: String): String {
    if (rawDigits.isBlank()) return ""
    val number = rawDigits.toLongOrNull() ?: return rawDigits
    return NumberFormat.getIntegerInstance(Locale("tr", "TR")).format(number)
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun GoalSetupScreenPreview() {
    KocKitTheme {
        GoalSetupScreenContent(
            uiState = GoalSetupUiState(
                universiteler = listOf(
                    YoUniversite(1, "Boğaziçi Üniversitesi"),
                    YoUniversite(2, "İstanbul Teknik Üniversitesi")
                ),
                bolumler = listOf(
                    YoBolum(52, 12, "İlahiyat"),
                    YoBolum(53, 12, "Bilgisayar Mühendisliği")
                ),
                onlyTyt = false,
                selectedPuanTurId = GoalSetupPuanTurOptions.PUAN_TUR_SAYISAL,
                siralamaInput = "50000",
                selectedUniversityType = null,
                selectedUniversityId = 1,
                selectedUniversityName = "Boğaziçi Üniversitesi",
                selectedBolumId = 52,
                selectedBolumName = "İlahiyat"
            ),
            onEvent = {}
        )
    }
}

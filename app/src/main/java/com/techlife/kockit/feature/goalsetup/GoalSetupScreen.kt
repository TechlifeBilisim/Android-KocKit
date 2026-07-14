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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.White
import com.techlife.kockit.domain.location.model.Province
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.UniversityType
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite
import kotlinx.coroutines.flow.collectLatest

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
                    GoalSetupExamStep(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
                GoalSetupContinueButton(
                    isLoading = uiState.isLoading,
                    onClick = { onEvent(GoalSetupEvent.ContinueClicked) },
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp)
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
private fun GoalSetupExamStep(
    uiState: GoalSetupUiState,
    onEvent: (GoalSetupEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitExtraBoldText(
        text = "Sınav Seçimi",
        color = TextPrimary,
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline
    )

    GoalSetupOnlyTytCard(
        onlyTyt = uiState.onlyTyt,
        onToggle = { onEvent(GoalSetupEvent.OnlyTytToggled(it)) }
    )
    uiState.examError?.let { KocKitText(text = it, color = colors.coralAccent) }

    GoalSetupSectionTitle(text = "Hedefini seç")
    GoalSetupUniversityTypeSwitch(
        selectedType = uiState.selectedUniversityType,
        onTypeSelected = { onEvent(GoalSetupEvent.UniversityTypeSelected(it)) }
    )
    uiState.universityTypeError?.let { KocKitText(text = it, color = colors.coralAccent) }

    KocKitDropdownField(
        label = "İl",
        options = uiState.provinces.map { it.name },
        selectedOption = uiState.selectedProvinceName,
        onOptionSelected = { name ->
            val province = uiState.provinces.find { it.name == name } ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.ProvinceSelected(province.id, province.name))
        },
        error = uiState.provinceError ?: uiState.provincesError,
        searchable = true,
        searchPlaceholder = "İl ara...",
        leadingIcon = Icons.Filled.LocationOn,
        leadingIconTint = OrangeAccent,
        leadingIconBackground = OrangeAccent.copy(alpha = 0.15f)
    )
    KocKitDropdownField(
        label = "İlçe",
        options = uiState.districts.map { it.name },
        selectedOption = uiState.selectedDistrictName,
        onOptionSelected = { name ->
            val district = uiState.districts.find { it.name == name } ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.DistrictSelected(district.id, district.name))
        },
        error = uiState.districtError ?: uiState.districtsError,
        searchable = true,
        searchPlaceholder = if (uiState.isDistrictsLoading) {
            "İlçeler yükleniyor..."
        } else if (uiState.selectedProvinceId == null) {
            "Önce il seçin"
        } else {
            "İlçe ara..."
        },
        leadingIcon = Icons.Filled.Place,
        leadingIconTint = LavenderAccent,
        leadingIconBackground = LavenderAccent.copy(alpha = 0.15f)
    )
    KocKitDropdownField(
        label = "Üniversite",
        options = uiState.universiteler.map { it.name },
        selectedOption = uiState.selectedUniversityName,
        onOptionSelected = { name ->
            val university = uiState.universiteler.find { it.name == name }
                ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.UniversitySelected(university.id, university.name))
        },
        error = uiState.universityError ?: uiState.universitelerError,
        searchable = true,
        searchPlaceholder = "Üniversite ara...",
        leadingIcon = Icons.Filled.School,
        leadingIconTint = PastelGreen,
        leadingIconBackground = PastelGreen.copy(alpha = 0.15f)
    )
    KocKitDropdownField(
        label = "Fakülte",
        options = uiState.fakulteler.map { it.name },
        selectedOption = uiState.selectedFakulteName,
        onOptionSelected = { name ->
            val fakulte = uiState.fakulteler.find { it.name == name } ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.FakulteSelected(fakulte.id, fakulte.name))
        },
        error = uiState.fakulteError ?: uiState.fakultelerError,
        searchable = true,
        searchPlaceholder = when {
            uiState.isFakultelerLoading -> "Fakülteler yükleniyor..."
            uiState.selectedUniversityId == null -> "Önce üniversite seçin"
            else -> "Fakülte ara..."
        },
        leadingIcon = Icons.Filled.AccountBalance,
        leadingIconTint = OrangeAccent,
        leadingIconBackground = OrangeAccent.copy(alpha = 0.15f)
    )
    KocKitDropdownField(
        label = "Bilim",
        options = uiState.bilimler.map { it.name },
        selectedOption = uiState.selectedBilimName,
        onOptionSelected = { name ->
            val bilim = uiState.bilimler.find { it.name == name } ?: return@KocKitDropdownField
            onEvent(GoalSetupEvent.BilimSelected(bilim.id, bilim.name))
        },
        error = uiState.bilimError ?: uiState.bilimlerError,
        searchable = true,
        searchPlaceholder = "Bilim ara...",
        leadingIcon = Icons.Filled.MenuBook,
        leadingIconTint = LavenderAccent,
        leadingIconBackground = LavenderAccent.copy(alpha = 0.12f)
    )
    KocKitDropdownField(
        label = "Bölüm",
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
            uiState.selectedBilimId == null && uiState.selectedUniversityId == null ->
                "Önce bilim veya üniversite seçin"
            else -> "Bölüm ara..."
        },
        leadingIcon = Icons.Filled.MenuBook,
        leadingIconTint = PastelGreen,
        leadingIconBackground = PastelGreen.copy(alpha = 0.12f)
    )
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
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
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
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(18.dp)
                    )
                }
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
            "Özel",
            iconPainter = null,
            iconVector = Icons.Filled.Person,
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
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(LavenderAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.School,
                contentDescription = null,
                tint = LavenderAccent,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        KocKitSemiText(
            text = "Sadece TYT'ye mi girmek istiyorsunuz?",
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

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun GoalSetupScreenPreview() {
    KocKitTheme {
        GoalSetupScreenContent(
            uiState = GoalSetupUiState(
                examGoals = listOf(
                    ExamGoal("tyt", "TYT", "Temel Yeterlilik Testi", "tyt"),
                    ExamGoal("ayt", "AYT", "Alan Yeterlilik Testi", "ayt"),
                ),
                universiteler = listOf(
                    YoUniversite(1, "Boğaziçi Üniversitesi"),
                    YoUniversite(2, "İstanbul Teknik Üniversitesi")
                ),
                fakulteler = listOf(
                    YoFakulte(1, 1, "Mühendislik Fak."),
                    YoFakulte(2, 1, "Fen-Edebiyat Fak.")
                ),
                bilimler = listOf(
                    YoBilim(1, "Mühendislik Bilimleri"),
                    YoBilim(12, "Din Bilimleri")
                ),
                bolumler = listOf(
                    YoBolum(52, 12, "İlahiyat")
                ),
                selectedExamGoalId = "tyt",
                provinces = listOf(Province(id = 34, name = "İstanbul")),
                selectedProvinceId = 34,
                selectedProvinceName = "İstanbul",
                districts = listOf(
                    com.techlife.kockit.domain.location.model.District(
                        id = 1,
                        name = "Kadıköy",
                        provinceId = 34,
                        provinceName = "İstanbul"
                    )
                ),
                selectedDistrictId = 1,
                selectedDistrictName = "Kadıköy",
                selectedUniversityType = null,
                onlyTyt = true,
                selectedUniversityId = 1,
                selectedUniversityName = "Boğaziçi Üniversitesi",
                selectedFakulteId = 1,
                selectedFakulteName = "Mühendislik Fak.",
                selectedBilimId = 12,
                selectedBilimName = "Din Bilimleri",
                selectedBolumId = 52,
                selectedBolumName = "İlahiyat"
            ),
            onEvent = {}
        )
    }
}

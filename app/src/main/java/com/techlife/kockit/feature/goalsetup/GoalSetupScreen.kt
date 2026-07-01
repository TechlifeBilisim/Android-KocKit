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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSelectableCard
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.CardShape
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.White
import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.domain.onboarding.model.UniversityType
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
                        .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalSetupExamStep(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
                KocKitPrimaryButton(
                    text = "Devam Et",
                    onClick = { onEvent(GoalSetupEvent.ContinueClicked) },
                    isLoading = uiState.isLoading,
                    showTrailingArrow = false,
                    containerColor = PastelGreen,
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
        color = Color.Black,
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline
    )
    KocKitBoldText(text = "Hangi sınava hazırlanıyorsun?")

    Spacer(modifier = Modifier.height(8.dp))

    val otherCardColors = listOf(LavenderAccent, OrangeAccent)
    val examIcons = mapOf(
        "tyt" to Icons.Outlined.Assignment,
        "ayt" to Icons.Filled.MenuBook,
    )
    uiState.examGoals.forEachIndexed { index, goal ->
        val cardColor = if (goal.id == "tyt") {
            PastelGreen
        } else {
            otherCardColors.getOrElse(index - 1) { colors.cardBackground }
        }
        if (goal.id == "ayt") {
            GoalSetupAytExamCard(
                goal = goal,
                backgroundColor = cardColor,
                isSelected = uiState.selectedExamGoalId == goal.id,
                fieldOptions = uiState.aytFieldOptions,
                selectedFieldId = uiState.selectedAytFieldId,
                fieldError = uiState.aytFieldError,
                onExamSelected = { onEvent(GoalSetupEvent.ExamGoalSelected(goal.id)) },
                onFieldSelected = { onEvent(GoalSetupEvent.AytFieldSelected(it)) }
            )
        } else {
            KocKitSelectableCard(
                title = goal.title,
                subtitle = goal.subtitle,
                backgroundColor = cardColor,
                leadingIcon = examIcons[goal.id] ?: Icons.Filled.MenuBook,
                isSelected = uiState.selectedExamGoalId == goal.id,
                onClick = { onEvent(GoalSetupEvent.ExamGoalSelected(goal.id)) }
            )
        }
    }
    uiState.examError?.let { KocKitText(text = it, color = colors.coralAccent) }

    Spacer(modifier = Modifier.height(16.dp))

    KocKitBoldText(
        text = "Hedefini seç",
        color = Color.Black,
        fontSize = KocKitTextDefaults.fontSizeTitle,
        lineHeight = KocKitTextDefaults.lineHeightTitle
    )
    GoalSetupUniversityTypeSwitch(
        selectedType = uiState.selectedUniversityType,
        onTypeSelected = { onEvent(GoalSetupEvent.UniversityTypeSelected(it)) }
    )
    uiState.universityTypeError?.let { KocKitText(text = it, color = colors.coralAccent) }
    KocKitDropdownField(
        label = "Bölge",
        options = uiState.availableRegions,
        selectedOption = uiState.selectedRegion,
        onOptionSelected = { onEvent(GoalSetupEvent.RegionSelected(it)) },
        error = uiState.regionError,
        searchable = true,
        searchPlaceholder = "Bölge ara..."
    )
    KocKitDropdownField(
        label = "İl",
        options = uiState.availableCities,
        selectedOption = uiState.selectedCity,
        onOptionSelected = { onEvent(GoalSetupEvent.CitySelected(it)) },
        error = uiState.cityError,
        searchable = true,
        searchPlaceholder = "İl ara..."
    )

    KocKitDropdownField(
        label = "Üniversite",
        options = uiState.universities.map { it.name },
        selectedOption = uiState.selectedUniversityName,
        onOptionSelected = { onEvent(GoalSetupEvent.UniversitySelected(it)) },
        error = uiState.universityError,
        searchable = true,
        searchPlaceholder = "Üniversite ara..."
    )
    KocKitDropdownField(
        label = "Bölüm",
        options = uiState.departments.map { it.name },
        selectedOption = uiState.selectedDepartmentName,
        onOptionSelected = { onEvent(GoalSetupEvent.DepartmentSelected(it)) },
        error = uiState.departmentError,
        searchable = true,
        searchPlaceholder = "Bölüm ara..."
    )
}

private val GoalSetupAttachedCardRadius = 20.dp

@Composable
private fun GoalSetupAytExamCard(
    goal: ExamGoal,
    backgroundColor: Color,
    isSelected: Boolean,
    fieldOptions: List<GoalSetupOption>,
    selectedFieldId: String?,
    fieldError: String?,
    onExamSelected: () -> Unit,
    onFieldSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isExpanded = isSelected
    val topShape = if (isExpanded) {
        RoundedCornerShape(
            topStart = GoalSetupAttachedCardRadius,
            topEnd = GoalSetupAttachedCardRadius
        )
    } else {
        CardShape
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(GoalSetupAttachedCardRadius)
                    )
                } else {
                    Modifier
                }
            )
    ) {
        KocKitSelectableCard(
            title = goal.title,
            subtitle = goal.subtitle,
            backgroundColor = backgroundColor,
            leadingIcon = Icons.Filled.MenuBook,
            isSelected = isSelected,
            onClick = onExamSelected,
            shape = topShape,
            showSelectionBorder = false,
            showExpandArrow = true
        )
        if (isExpanded) {
            GoalSetupAytFieldPanel(
                fieldOptions = fieldOptions,
                selectedFieldId = selectedFieldId,
                backgroundColor = backgroundColor,
                onFieldSelected = onFieldSelected,
                error = fieldError
            )
        }
    }
}

@Composable
private fun GoalSetupAytFieldPanel(
    fieldOptions: List<GoalSetupOption>,
    selectedFieldId: String?,
    backgroundColor: Color,
    onFieldSelected: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = GoalSetupAttachedCardRadius,
                    bottomEnd = GoalSetupAttachedCardRadius
                )
            )
            .background(backgroundColor)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        KocKitSemiText(
            text = "Alan Seçimi",
            color = White.copy(alpha = 0.9f),
            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
        )
        fieldOptions.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowOptions.forEach { option ->
                    GoalSetupAytFieldOption(
                        label = option.label,
                        isSelected = selectedFieldId == option.id,
                        backgroundColor = backgroundColor,
                        onClick = { onFieldSelected(option.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowOptions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        error?.let {
            KocKitText(
                text = it,
                color = White
            )
        }
    }
}

@Composable
private fun GoalSetupAytFieldOption(
    label: String,
    isSelected: Boolean,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipBackground = if (isSelected) White else White.copy(alpha = 0.18f)
    val borderColor = if (isSelected) White else White.copy(alpha = 0.35f)
    val textColor = if (isSelected) backgroundColor else White

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(chipBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        KocKitSemiText(
            text = label,
            color = textColor,
            fontSize = KocKitTextDefaults.fontSizeBody,
            lineHeight = KocKitTextDefaults.lineHeightBody,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun GoalSetupUniversityTypeSwitch(
    selectedType: UniversityType?,
    onTypeSelected: (UniversityType) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.borderLight.copy(alpha = 0.45f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        UniversityType.entries.forEach { type ->
            val isSelected = selectedType == type
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) PastelGreen else Color.Transparent)
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                KocKitSemiText(
                    text = type.label,
                    color = if (isSelected) Color.White else colors.textPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
            }
        }
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
                universities = listOf(
                    University("u001", "Boğaziçi Üniversitesi", "İstanbul", "Marmara", UniversityType.DEVLET),
                    University("u002", "İstanbul Teknik Üniversitesi", "İstanbul", "Marmara", UniversityType.DEVLET)
                ),
                departments = listOf(
                    Department("d01", "Bilgisayar Mühendisliği"),
                    Department("d02", "Yazılım Mühendisliği")
                ),
                selectedExamGoalId = "tyt",
                selectedRegion = "Marmara",
                selectedCity = "İstanbul",
                selectedUniversityType = UniversityType.DEVLET,
                selectedUniversityName = "Boğaziçi Üniversitesi",
                selectedDepartmentName = "Bilgisayar Mühendisliği"
            ),
            onEvent = {}
        )
    }
}

package com.techlife.kockit.feature.goalsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSelectableCard
import com.techlife.kockit.core.designsystem.component.KocKitSimpleSelectableCard
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GoalSetupScreen(
    viewModel: GoalSetupViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = KocKitTheme.extraColors

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GoalSetupEffect.NavigateToHome -> onNavigateToHome()
                GoalSetupEffect.NavigateBack -> onNavigateBack()
                is GoalSetupEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    KocKitBackground {
        Column(Modifier.fillMaxSize()) {
            KocKitTopBar(onBackClick = { viewModel.onEvent(GoalSetupEvent.BackClicked) })
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (uiState.currentStep) {
                    GoalSetupSteps.EXAM_AND_TARGET -> GoalSetupExamStep(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                    GoalSetupSteps.STUDY_TIME -> GoalSetupStudyTimeStep(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                    GoalSetupSteps.RANK_GOAL -> GoalSetupRankGoalStep(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                }
            }
            KocKitPrimaryButton(
                text = "Devam Et",
                onClick = { viewModel.onEvent(GoalSetupEvent.ContinueClicked) },
                isLoading = uiState.isLoading,
                showTrailingArrow = true,
                containerColor = PastelGreen,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
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
    uiState.examGoals.forEachIndexed { index, goal ->
        val cardColor = if (goal.id == "tyt") {
            PastelGreen
        } else {
            otherCardColors.getOrElse(index - 1) { colors.cardBackground }
        }
        KocKitSelectableCard(
            title = goal.title,
            subtitle = goal.subtitle,
            backgroundColor = cardColor,
            leadingIcon = Icons.Filled.School,
            isSelected = uiState.selectedExamGoalId == goal.id,
            onClick = { onEvent(GoalSetupEvent.ExamGoalSelected(goal.id)) }
        )
    }
    uiState.examError?.let { KocKitText(text = it, color = colors.coralAccent) }

    Spacer(modifier = Modifier.height(16.dp))

    KocKitBoldText(
        text = "Hedefini seç",
        color = Color.Black,
        fontSize = KocKitTextDefaults.fontSizeTitle,
        lineHeight = KocKitTextDefaults.lineHeightTitle
    )
    KocKitDropdownField(
        label = "Üniversite",
        options = uiState.universities.map { it.name },
        selectedOption = uiState.selectedUniversityName,
        onOptionSelected = { onEvent(GoalSetupEvent.UniversitySelected(it)) },
        error = uiState.universityError
    )
    KocKitDropdownField(
        label = "Bölüm",
        options = uiState.departments.map { it.name },
        selectedOption = uiState.selectedDepartmentName,
        onOptionSelected = { onEvent(GoalSetupEvent.DepartmentSelected(it)) },
        error = uiState.departmentError
    )
}

@Composable
private fun GoalSetupStudyTimeStep(
    uiState: GoalSetupUiState,
    onEvent: (GoalSetupEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitBoldText(
        text = "Günlük ortalama çalışma süren?",
        color = Color.Black,
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline
    )

    Spacer(modifier = Modifier.height(8.dp))

    uiState.studyTimeOptions.forEach { option ->
        KocKitSimpleSelectableCard(
            label = option.label,
            isSelected = uiState.selectedStudyTimeId == option.id,
            onClick = { onEvent(GoalSetupEvent.StudyTimeSelected(option.id)) }
        )
    }
    uiState.studyTimeError?.let { KocKitText(text = it, color = colors.coralAccent) }
}

@Composable
private fun GoalSetupRankGoalStep(
    uiState: GoalSetupUiState,
    onEvent: (GoalSetupEvent) -> Unit
) {
    val colors = KocKitTheme.extraColors

    KocKitBoldText(
        text = "Hedefin nedir?",
        color = Color.Black,
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline
    )

    Spacer(modifier = Modifier.height(8.dp))

    uiState.rankGoalOptions.forEach { option ->
        KocKitSimpleSelectableCard(
            label = option.label,
            isSelected = uiState.selectedRankGoalId == option.id,
            onClick = { onEvent(GoalSetupEvent.RankGoalSelected(option.id)) }
        )
    }
    uiState.rankGoalError?.let { KocKitText(text = it, color = colors.coralAccent) }
}

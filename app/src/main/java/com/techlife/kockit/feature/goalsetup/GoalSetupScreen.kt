package com.techlife.kockit.feature.goalsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSelectableCard
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
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KocKitBoldText(
                    text = "Sınav Seçimi",
                    fontSize = KocKitTextDefaults.fontSizeHeadline,
                    lineHeight = KocKitTextDefaults.lineHeightHeadline
                )
                KocKitText("Hangi sınava hazırlanıyorsun?")
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
                        onClick = { viewModel.onEvent(GoalSetupEvent.ExamGoalSelected(goal.id)) }
                    )
                }
                uiState.examError?.let { KocKitText(text = it, color = colors.coralAccent) }
                KocKitSemiText(
                    text = "Hedefini seç",
                    fontSize = KocKitTextDefaults.fontSizeTitle,
                    lineHeight = KocKitTextDefaults.lineHeightTitle
                )
                KocKitDropdownField(
                    label = "Üniversite",
                    options = uiState.universities.map { it.name },
                    selectedOption = uiState.selectedUniversityName,
                    onOptionSelected = { viewModel.onEvent(GoalSetupEvent.UniversitySelected(it)) },
                    error = uiState.universityError
                )
                KocKitDropdownField(
                    label = "Bölüm",
                    options = uiState.departments.map { it.name },
                    selectedOption = uiState.selectedDepartmentName,
                    onOptionSelected = { viewModel.onEvent(GoalSetupEvent.DepartmentSelected(it)) },
                    error = uiState.departmentError
                )
                KocKitPrimaryButton(
                    text = "Devam Et",
                    onClick = { viewModel.onEvent(GoalSetupEvent.ContinueClicked) },
                    isLoading = uiState.isLoading,
                    showTrailingArrow = true,
                    containerColor = PastelGreen
                )
            }
        }
    }
}

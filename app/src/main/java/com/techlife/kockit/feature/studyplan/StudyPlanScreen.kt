package com.techlife.kockit.feature.studyplan

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.layout.StudyPlanContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberStudyPlanLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StudyPlanScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudyPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showAddSpecialDateSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StudyPlanEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (showAddSpecialDateSheet) {
        StudyPlanAddSpecialDateSheet(
            onDismiss = { showAddSpecialDateSheet = false },
            onAdd = { newDate ->
                viewModel.onEvent(StudyPlanEvent.SpecialDateAdded(newDate))
                showAddSpecialDateSheet = false
            }
        )
    }

    StudyPlanScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
        onAddSpecialDateClick = { showAddSpecialDateSheet = true },
        modifier = modifier
    )
}

@Composable
fun StudyPlanScreenContent(
    uiState: StudyPlanUiState,
    onBackClick: () -> Unit,
    onEvent: (StudyPlanEvent) -> Unit,
    onAddSpecialDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = rememberStudyPlanLayoutMetrics()

    Box(modifier = modifier.fillMaxSize()) {
        StudyPlanContentContainer(metrics = metrics) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBackground)
            ) {
                item(key = "header") {
                    Spacer(modifier = Modifier.height(metrics.topInset))
                    StudyPlanHeader(
                        onBackClick = onBackClick,
                        onAutoPlanClick = { },
                        description = StudyPlanFakeData.DESCRIPTION
                    )
                    Spacer(modifier = Modifier.height(metrics.sectionSpacing + 8.dp))
                }

                item(key = "days") {
                    StudyPlanDaysSection(
                        days = uiState.days,
                        totalHours = uiState.totalHours,
                        isEditing = uiState.daysEditing,
                        onEditClick = { onEvent(StudyPlanEvent.DaysEditClicked) },
                        onSaveClick = { onEvent(StudyPlanEvent.DaysSaveClicked) },
                        onHoursChange = { index, newHours ->
                            onEvent(StudyPlanEvent.DayHoursChanged(index, newHours))
                        }
                    )
                    Spacer(modifier = Modifier.height(metrics.sectionSpacing))
                }

                item(key = "parameters") {
                    StudyPlanParametersSection(
                        sessionMinutes = uiState.sessionMinutes,
                        onSessionChange = { onEvent(StudyPlanEvent.SessionMinutesChanged(it)) },
                        paragraphMinutes = uiState.paragraphMinutes,
                        onParagraphChange = { onEvent(StudyPlanEvent.ParagraphMinutesChanged(it)) },
                        problemMinutes = uiState.problemMinutes,
                        onProblemChange = { onEvent(StudyPlanEvent.ProblemMinutesChanged(it)) },
                        revisionDay = uiState.revisionDay,
                        onRevisionDayChange = { onEvent(StudyPlanEvent.RevisionDayChanged(it)) },
                        revisionOptions = StudyPlanFakeData.revisionDayOptions,
                        isEditing = uiState.parametersEditing,
                        onEditClick = { onEvent(StudyPlanEvent.ParametersEditClicked) },
                        onSaveClick = { onEvent(StudyPlanEvent.ParametersSaveClicked) }
                    )
                    Spacer(modifier = Modifier.height(metrics.sectionSpacing))
                }

                item(key = "unavailable") {
                    StudyPlanUnavailableSection(
                        weekDayOptions = StudyPlanFakeData.weekDayOptions,
                        selectedUnavailableDays = uiState.unavailableDays,
                        specialDates = uiState.specialDates,
                        isEditing = uiState.unavailableEditing,
                        onEditClick = { onEvent(StudyPlanEvent.UnavailableEditClicked) },
                        onSaveClick = { onEvent(StudyPlanEvent.UnavailableSaveClicked) },
                        onUnavailableDayToggle = { shortName ->
                            onEvent(StudyPlanEvent.UnavailableDayToggled(shortName))
                        },
                        onRemoveSpecialDate = { id ->
                            onEvent(StudyPlanEvent.SpecialDateRemoved(id))
                        },
                        onAddSpecialDateClick = onAddSpecialDateClick
                    )
                    Spacer(modifier = Modifier.height(metrics.sectionSpacing + 4.dp))
                }

                item(key = "info") {
                    StudyPlanInfoNote(text = StudyPlanFakeData.SAVE_INFO_NOTE)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item(key = "save") {
                    StudyPlanSaveButton(
                        onClick = { onEvent(StudyPlanEvent.SaveClicked) },
                        enabled = !uiState.isSaving && !uiState.isLoading,
                        isLoading = uiState.isSaving
                    )
                    Spacer(modifier = Modifier.height(metrics.topInset + 8.dp))
                }
            }
        }

        if (uiState.isLoading && !uiState.isDataLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBackground.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PastelGreen)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
private fun StudyPlanScreenTabletPreview() {
    KocKitTheme {
        StudyPlanScreenContent(
            uiState = StudyPlanUiState(),
            onBackClick = {},
            onEvent = {},
            onAddSpecialDateClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StudyPlanScreenPreview() {
    KocKitTheme {
        StudyPlanScreenContent(
            uiState = StudyPlanUiState(),
            onBackClick = {},
            onEvent = {},
            onAddSpecialDateClick = {}
        )
    }
}

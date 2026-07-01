package com.techlife.kockit.feature.placementtest

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.layout.PlacementTestContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberPlacementTestLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.White

@Composable
fun PlacementTestExamScreen(
    section: PlacementTestSection,
    viewModel: PlacementTestViewModel,
    onBackClick: () -> Unit,
    onFinishExam: () -> Unit
) {
    val examState by viewModel.examState.collectAsStateWithLifecycle()

    PlacementTestExamContent(
        section = section,
        examState = examState,
        onBackClick = onBackClick,
        onFinishExam = onFinishExam,
        onSelectOption = viewModel::selectOption,
        onNextClick = viewModel::goToNextQuestion
    )
}

@Composable
fun PlacementTestExamContent(
    section: PlacementTestSection,
    examState: PlacementExamUiState,
    onBackClick: () -> Unit,
    onFinishExam: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onNextClick: () -> Unit,
) {
    val metrics = rememberPlacementTestLayoutMetrics()
    val questionIndex = examState.currentQuestionIndex.coerceIn(
        0,
        (examState.questions.size - 1).coerceAtLeast(0)
    )
    val question = examState.questions.getOrNull(questionIndex)
    val isLastQuestion = examState.currentQuestionIndex >= examState.totalQuestions - 1

    PlacementTestDecorBackground(accentSoftColor = section.accentSoftColor) {
        PlacementTestContentContainer(metrics = metrics) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = metrics.topInset / 2, bottom = 8.dp)
                ) {
                    PlacementTestExamHeader(
                        title = section.examTitle,
                        onBackClick = onBackClick
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KocKitSemiText(
                            text = "${examState.currentQuestionIndex + 1}/${examState.totalQuestions}",
                            color = TextPrimary,
                            fontSize = metrics.bodyLargeSize,
                            lineHeight = metrics.bodyLargeLineHeight
                        )
                        PlacementTimerBadge(
                            timerText = examState.timerText,
                            accentColor = PlacementTestColors.green
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PlacementSegmentedProgress(
                        currentIndex = examState.currentQuestionIndex,
                        totalQuestions = examState.totalQuestions,
                        accentColor = PlacementTestColors.green
                    )
                }

                question?.let { current ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(14.dp))

                        PlacementSubjectChip(
                            subject = current.subject,
                            accentColor = section.accentColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (current.prompt.isNotBlank()) {
                            KocKitBoldText(
                                text = current.prompt,
                                color = TextPrimary,
                                fontSize = metrics.bodyLargeSize,
                                lineHeight = metrics.bodyLargeLineHeight
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (current.detail.isNotBlank()) {
                            KocKitSemiText(
                                text = current.detail,
                                color = TextPrimary,
                                fontSize = metrics.bodySize,
                                lineHeight = metrics.bodyLineHeight
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        PlacementQuestionImage(imageResId = current.imageResId)
                    }
                } ?: Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = metrics.bottomInset)
                ) {
                    PlacementAnswerRadioRow(
                        selectedIndex = examState.selectedOptionIndex,
                        onOptionSelected = onSelectOption,
                        accentColor = PlacementTestColors.green
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = {
                                if (isLastQuestion) onFinishExam()
                                else onNextClick()
                            },
                            enabled = examState.selectedOptionIndex != null,
                            modifier = Modifier.height(metrics.primaryButtonHeight),
                            shape = RoundedCornerShape(metrics.answerOptionCornerRadius),
                            colors = ButtonDefaults.buttonColors(containerColor = PlacementTestColors.green)
                        ) {
                            KocKitSemiText(
                                text = if (isLastQuestion) "Bitir" else "Sonraki",
                                color = White,
                                fontSize = metrics.primaryButtonTextSize,
                                lineHeight = metrics.bodyLargeLineHeight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlacementTestExamScreenPreview() {
    KocKitTheme {
        PlacementTestExamContent(
            section = PlacementTestSection.GENERAL_ABILITY,
            examState = PlacementExamUiState(
                currentQuestionIndex = 0,
                timerText = "03:45",
                selectedOptionIndex = 1,
                questions = listOf(
                    PlacementQuestion(
                        prompt = "Soru 1",
                        detail = "Grafikte verilen fonksiyon için doğru seçeneği işaretleyin.",
                        subject = "Matematik"
                    )
                )
            ),
            onBackClick = {},
            onFinishExam = {},
            onSelectOption = {},
            onNextClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
fun PlacementTestExamScreenTabletPreview() {
    KocKitTheme {
        PlacementTestExamContent(
            section = PlacementTestSection.GENERAL_ABILITY,
            examState = PlacementExamUiState(
                currentQuestionIndex = 0,
                timerText = "03:45",
                selectedOptionIndex = 2,
                questions = listOf(
                    PlacementQuestion(
                        prompt = "Soru 1",
                        detail = "Grafikte verilen fonksiyon için doğru seçeneği işaretleyin.",
                        subject = "Matematik"
                    )
                )
            ),
            onBackClick = {},
            onFinishExam = {},
            onSelectOption = {},
            onNextClick = {}
        )
    }
}

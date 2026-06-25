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
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val optionLabels = listOf("A", "B", "C", "D", "E")
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

                    question?.let { current ->
                        Spacer(modifier = Modifier.height(14.dp))

                        PlacementSubjectChip(
                            subject = current.subject,
                            accentColor = section.accentColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (current.prompt.isNotBlank()) {
                            KocKitBoldText(
                                text = current.prompt,
                                color = TextPrimary,
                                fontSize = metrics.bodyLargeSize,
                                lineHeight = metrics.bodyLargeLineHeight
                            )
                        }
                        if (current.detail.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            KocKitSemiText(
                                text = current.detail,
                                color = TextPrimary,
                                fontSize = metrics.bodyLargeSize,
                                lineHeight = metrics.bodyLargeLineHeight
                            )
                        }
                    }
                }

                question?.let { current ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        current.options.forEachIndexed { index, optionText ->
                            PlacementAnswerOption(
                                label = optionLabels.getOrElse(index) { "" },
                                text = optionText,
                                isSelected = examState.selectedOptionIndex == index,
                                accentColor = PlacementTestColors.green,
                                onClick = { onSelectOption(index) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } ?: Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = metrics.bottomInset),
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
                        prompt = "1. x > 2 olmak üzere,",
                        detail = "f(x) = (x² - 4) / (x - 2) fonksiyonunun lim(x→2) f(x) değeri kaçtır?",
                        options = listOf("0", "2", "4", "-2", "1"),
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
                selectedOptionIndex = 1,
                questions = listOf(
                    PlacementQuestion(
                        prompt = "1. x > 2 olmak üzere,",
                        detail = "f(x) = (x² - 4) / (x - 2) fonksiyonunun lim(x→2) f(x) değeri kaçtır?",
                        options = listOf("0", "2", "4", "-2", "1"),
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

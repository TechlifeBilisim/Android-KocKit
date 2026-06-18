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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
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
    val questionIndex = examState.currentQuestionIndex.coerceIn(
        0,
        (examState.questions.size - 1).coerceAtLeast(0)
    )
    val question = examState.questions.getOrNull(questionIndex)
    val optionLabels = listOf("A", "B", "C", "D", "E")
    val isLastQuestion = examState.currentQuestionIndex >= examState.totalQuestions - 1

    PlacementTestDecorBackground(accentSoftColor = section.accentSoftColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                PlacementExamTopBar(title = section.examTitle)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KocKitSemiText(
                        text = "${examState.currentQuestionIndex + 1}/${examState.totalQuestions}",
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
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
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                        )
                    }
                    if (current.detail.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        KocKitSemiText(
                            text = current.detail,
                            color = TextPrimary,
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                        )
                    }
                }
            }

            question?.let { current ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    current.options.forEachIndexed { index, optionText ->
                        PlacementAnswerOption(
                            label = optionLabels.getOrElse(index) { "" },
                            text = optionText,
                            isSelected = examState.selectedOptionIndex == index,
                            accentColor = PlacementTestColors.green,
                            onClick = { viewModel.selectOption(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } ?: Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        if (isLastQuestion) onFinishExam()
                        else viewModel.goToNextQuestion()
                    },
                    enabled = examState.selectedOptionIndex != null,
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PlacementTestColors.green)
                ) {
                    KocKitSemiText(
                        text = if (isLastQuestion) "Bitir" else "Sonraki",
                        color = White,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                    )
                }
            }
        }
    }
}

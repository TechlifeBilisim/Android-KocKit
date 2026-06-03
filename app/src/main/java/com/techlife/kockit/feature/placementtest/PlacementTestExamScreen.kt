package com.techlife.kockit.feature.placementtest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.theme.PastelGreen
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
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                PlacementTestExamHeader(
                    title = section.examTitle,
                    onBackClick = onBackClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                PlacementExamStatusBar(
                    timerText = examState.timerText,
                    questionLabel = "Soru ${examState.currentQuestionIndex + 1} / ${examState.totalQuestions}",
                    currentQuestionIndex = examState.currentQuestionIndex,
                    totalQuestions = examState.totalQuestions
                )

                Spacer(modifier = Modifier.height(20.dp))

                question?.let { current ->
                    KocKitBoldText(
                        text = current.prompt,
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    KocKitSemiText(
                        text = current.detail,
                        color = TextPrimary,
                        fontSize = KocKitTextDefaults.fontSizeTitle,
                        lineHeight = KocKitTextDefaults.lineHeightTitle
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    current.options.forEachIndexed { index, optionText ->
                        PlacementAnswerOption(
                            label = optionLabels.getOrElse(index) { "" },
                            text = optionText,
                            isSelected = examState.selectedOptionIndex == index,
                            accentColor = section.accentColor,
                            onClick = { viewModel.selectOption(index) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.goToPreviousQuestion() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = White)
                    ) {
                        KocKitSemiText(
                            text = "Önceki",
                            color = TextPrimary,
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                        )
                    }
                    Button(
                        onClick = {
                            if (isLastQuestion) onFinishExam()
                            else viewModel.goToNextQuestion()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PastelGreen)
                    ) {
                        KocKitSemiText(
                            text = if (isLastQuestion) "Bitir" else "Sonraki",
                            color = White,
                            fontSize = KocKitTextDefaults.fontSizeBodyLarge,
                            lineHeight = KocKitTextDefaults.lineHeightBodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

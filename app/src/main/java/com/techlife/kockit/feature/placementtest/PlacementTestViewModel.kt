package com.techlife.kockit.feature.placementtest

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlacementExamUiState(
    val currentQuestionIndex: Int = 0,
    val timerText: String = "03:45",
    val selectedOptionIndex: Int? = null,
    val answers: Map<Int, Int> = emptyMap(),
    val questions: List<PlacementQuestion> = emptyList()
) {
    val totalQuestions: Int get() = questions.size
}

@HiltViewModel
class PlacementTestViewModel @Inject constructor() : ViewModel() {

    private val _examState = MutableStateFlow(PlacementExamUiState())
    val examState: StateFlow<PlacementExamUiState> = _examState.asStateFlow()

    fun loadExam(section: PlacementTestSection) {
        _examState.value = PlacementExamUiState(
            currentQuestionIndex = 0,
            selectedOptionIndex = null,
            questions = PlacementTestFakeData.questionsFor(section)
        )
    }

    fun selectOption(index: Int) {
        _examState.update { state ->
            state.copy(
                selectedOptionIndex = index,
                answers = state.answers + (state.currentQuestionIndex to index)
            )
        }
    }

    fun goToNextQuestion() {
        _examState.update { state ->
            if (state.currentQuestionIndex >= state.totalQuestions - 1) {
                state
            } else {
                val nextIndex = state.currentQuestionIndex + 1
                state.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOptionIndex = state.answers[nextIndex]
                )
            }
        }
    }

    fun goToPreviousQuestion() {
        _examState.update { state ->
            if (state.currentQuestionIndex <= 0) {
                state
            } else {
                val previousIndex = state.currentQuestionIndex - 1
                state.copy(
                    currentQuestionIndex = previousIndex,
                    selectedOptionIndex = state.answers[previousIndex]
                )
            }
        }
    }
}

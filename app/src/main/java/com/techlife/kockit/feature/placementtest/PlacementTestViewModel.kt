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
        _examState.update { it.copy(selectedOptionIndex = index) }
    }

    fun goToNextQuestion() {
        _examState.update { state ->
            if (state.currentQuestionIndex >= state.totalQuestions - 1) state
            else state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1,
                selectedOptionIndex = null
            )
        }
    }
}

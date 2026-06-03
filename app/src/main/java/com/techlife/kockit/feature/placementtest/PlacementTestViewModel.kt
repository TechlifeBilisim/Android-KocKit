package com.techlife.kockit.feature.placementtest

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlacementQuestion(
    val prompt: String,
    val detail: String,
    val options: List<String>
)

data class PlacementExamUiState(
    val currentQuestionIndex: Int = 0,
    val timerText: String = "29:12",
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
        val questions = fakeQuestions(section)
        _examState.value = PlacementExamUiState(
            currentQuestionIndex = 0,
            selectedOptionIndex = null,
            questions = questions
        )
    }

    fun selectOption(index: Int) {
        _examState.update { it.copy(selectedOptionIndex = index) }
    }

    fun goToPreviousQuestion() {
        _examState.update { state ->
            if (state.currentQuestionIndex <= 0) state
            else state.copy(
                currentQuestionIndex = state.currentQuestionIndex - 1,
                selectedOptionIndex = null
            )
        }
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

    private fun fakeQuestions(section: PlacementTestSection): List<PlacementQuestion> {
        if (section == PlacementTestSection.GENERAL_CULTURE) {
            return listOf(
                PlacementQuestion(
                    prompt = "Aşağıdakilerden hangisi Cumhuriyet'in ilan edildiği yıldır?",
                    detail = "Soru 1",
                    options = listOf("1919", "1920", "1921", "1922", "1923")
                ),
                PlacementQuestion(
                    prompt = "Türkiye'nin başkenti aşağıdakilerden hangisidir?",
                    detail = "Soru 2",
                    options = listOf("İstanbul", "Ankara", "İzmir", "Bursa", "Antalya")
                )
            )
        }
        return listOf(
            PlacementQuestion(
                prompt = "Aşağıdaki sayı dizisinde soru işareti yerine gelmesi gereken sayı kaçtır?",
                detail = "2, 6, 12, 20, 30, ?",
                options = listOf("38", "40", "42", "44", "46")
            ),
            PlacementQuestion(
                prompt = "Aşağıdaki ifadelerden hangisi verilen koşulları sağlar?",
                detail = "Soru 2",
                options = listOf("A", "B", "C", "D", "E")
            )
        )
    }
}

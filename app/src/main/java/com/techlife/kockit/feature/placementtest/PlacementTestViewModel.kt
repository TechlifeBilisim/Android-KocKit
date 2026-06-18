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
    val options: List<String>,
    val subject: String = "Matematik"
)

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
            questions = fakeQuestions(section)
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

    private fun fakeQuestions(section: PlacementTestSection): List<PlacementQuestion> {
        if (section == PlacementTestSection.GENERAL_CULTURE) {
            return listOf(
                PlacementQuestion(
                    prompt = "Aşağıdakilerden hangisi Cumhuriyet'in ilan edildiği yıldır?",
                    detail = "",
                    options = listOf("1919", "1920", "1921", "1922", "1923"),
                    subject = "Tarih"
                ),
                PlacementQuestion(
                    prompt = "Türkiye'nin başkenti aşağıdakilerden hangisidir?",
                    detail = "",
                    options = listOf("İstanbul", "Ankara", "İzmir", "Bursa", "Antalya"),
                    subject = "Coğrafya"
                ),
                PlacementQuestion(
                    prompt = "Aşağıdakilerden hangisi bir fizik kanunudur?",
                    detail = "",
                    options = listOf("Evrim", "Yerçekimi", "Fotosentez", "Mitoz", "Osmoz"),
                    subject = "Fen Bilimleri"
                )
            )
        }
        return listOf(
            PlacementQuestion(
                prompt = "1. x > 2 olmak üzere,",
                detail = "f(x) = (x² - 4) / (x - 2) fonksiyonunun lim(x→2) f(x) değeri kaçtır?",
                options = listOf("0", "2", "4", "-2", "1"),
                subject = "Matematik"
            ),
            PlacementQuestion(
                prompt = "Aşağıdaki sayı dizisinde soru işareti yerine gelmesi gereken sayı kaçtır?",
                detail = "2, 6, 12, 20, 30, ?",
                options = listOf("38", "40", "42", "44", "46"),
                subject = "Sayısal Yetenek"
            ),
            PlacementQuestion(
                prompt = "Verilen paragrafta yazarın asıl vurgulamak istediği düşünce hangisidir?",
                detail = "",
                options = listOf("A", "B", "C", "D", "E"),
                subject = "Sözel Yetenek"
            )
        )
    }
}

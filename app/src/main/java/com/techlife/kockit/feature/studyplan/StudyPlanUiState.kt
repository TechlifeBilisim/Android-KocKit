package com.techlife.kockit.feature.studyplan

data class StudyPlanUiState(
    val days: List<StudyPlanDay> = StudyPlanDayMapper.defaultDays(),
    val sessionMinutes: Int = StudyPlanFakeData.INITIAL_SESSION_MINUTES,
    val paragraphMinutes: Int = StudyPlanFakeData.INITIAL_PARAGRAPH_MINUTES,
    val problemMinutes: Int = StudyPlanFakeData.INITIAL_PROBLEM_MINUTES,
    val revisionDay: String = StudyPlanFakeData.INITIAL_REVISION_DAY,
    val unavailableDays: Set<String> = emptySet(),
    val specialDates: List<StudyPlanSpecialDate> = emptyList(),
    val daysEditing: Boolean = false,
    val parametersEditing: Boolean = false,
    val unavailableEditing: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDataLoaded: Boolean = false
) {
    val totalHours: Int get() = days.sumOf { it.hours }
}

sealed interface StudyPlanEvent {
    data class DayHoursChanged(val index: Int, val hours: Int) : StudyPlanEvent
    data class SessionMinutesChanged(val value: Int) : StudyPlanEvent
    data class ParagraphMinutesChanged(val value: Int) : StudyPlanEvent
    data class ProblemMinutesChanged(val value: Int) : StudyPlanEvent
    data class RevisionDayChanged(val value: String) : StudyPlanEvent
    data class UnavailableDayToggled(val shortName: String) : StudyPlanEvent
    data class SpecialDateAdded(val date: StudyPlanSpecialDate) : StudyPlanEvent
    data class SpecialDateRemoved(val id: String) : StudyPlanEvent
    data object DaysEditClicked : StudyPlanEvent
    data object DaysSaveClicked : StudyPlanEvent
    data object ParametersEditClicked : StudyPlanEvent
    data object ParametersSaveClicked : StudyPlanEvent
    data object UnavailableEditClicked : StudyPlanEvent
    data object UnavailableSaveClicked : StudyPlanEvent
    data object SaveClicked : StudyPlanEvent
}

sealed interface StudyPlanEffect {
    data class ShowMessage(val message: String) : StudyPlanEffect
}

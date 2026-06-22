package com.techlife.kockit.feature.studyplan

data class StudyPlanDay(
    val name: String,
    val hours: Int
)

object StudyPlanFakeData {
    val initialDays = listOf(
        StudyPlanDay("Pazartesi", 24),
        StudyPlanDay("Salı", 22),
        StudyPlanDay("Çarşamba", 11),
        StudyPlanDay("Perşembe", 11),
        StudyPlanDay("Cuma", 18),
        StudyPlanDay("Cumartesi", 23),
        StudyPlanDay("Pazar", 8)
    )

    const val INITIAL_STUDY_PERIOD_MINUTES = 50
    const val INITIAL_PARAGRAPH_MINUTES = 30
}

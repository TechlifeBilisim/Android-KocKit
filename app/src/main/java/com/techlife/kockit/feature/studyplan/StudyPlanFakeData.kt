package com.techlife.kockit.feature.studyplan

data class StudyPlanDay(
    val name: String,
    val shortName: String,
    val hours: Int,
    val showCheckmark: Boolean = true
)

data class StudyPlanSpecialDate(
    val id: String,
    val title: String,
    val subtitle: String
)

enum class StudyPlanSpecialDateMode {
    SINGLE,
    RANGE
}

data class StudyPlanWeekDayOption(
    val shortName: String,
    val fullName: String
)

object StudyPlanFakeData {
    val weekDayOptions = listOf(
        StudyPlanWeekDayOption("Pzt", "Pazartesi"),
        StudyPlanWeekDayOption("Sal", "Salı"),
        StudyPlanWeekDayOption("Çar", "Çarşamba"),
        StudyPlanWeekDayOption("Per", "Perşembe"),
        StudyPlanWeekDayOption("Cum", "Cuma"),
        StudyPlanWeekDayOption("Cmt", "Cumartesi"),
        StudyPlanWeekDayOption("Paz", "Pazar")
    )

    val revisionDayOptions = weekDayOptions.map { it.fullName }

    val initialDays = listOf(
        StudyPlanDay("Pazartesi", "Pzt", 3),
        StudyPlanDay("Salı", "Sal", 4),
        StudyPlanDay("Çarşamba", "Çar", 2),
        StudyPlanDay("Perşembe", "Per", 3),
        StudyPlanDay("Cuma", "Cum", 4),
        StudyPlanDay("Cumartesi", "Cmt", 5),
        StudyPlanDay("Pazar", "Paz", 2)
    )

    const val INITIAL_SESSION_MINUTES = 50
    const val INITIAL_PARAGRAPH_MINUTES = 30
    const val INITIAL_PROBLEM_MINUTES = 45
    const val INITIAL_REVISION_DAY = "Pazar"

    val initialUnavailableDays = setOf("Cmt", "Paz")

    val initialSpecialDates = listOf(
        StudyPlanSpecialDate(
            id = "1",
            title = "10 Temmuz 2026 - 20 Temmuz 2026",
            subtitle = "Yaz tatili"
        ),
        StudyPlanSpecialDate(
            id = "2",
            title = "27 Haziran 2026",
            subtitle = "Düğün"
        )
    )

    const val DESCRIPTION =
        "Çalışma planını düzenle, hedeflerine ulaşmak için programını kişiselleştir."

    const val SAVE_INFO_NOTE =
        "Planında değişiklik yaptığında, oluşturulan günlük planlar otomatik olarak güncellenecektir."
}

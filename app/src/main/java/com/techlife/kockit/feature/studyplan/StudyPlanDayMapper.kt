package com.techlife.kockit.feature.studyplan

object StudyPlanDayMapper {
    /** API: 1 = Pazartesi … 7 = Pazar */
    val weekDays: List<StudyPlanWeekDayOption> = StudyPlanFakeData.weekDayOptions

    fun dayIndexFromFullName(fullName: String): Int? =
        weekDays.indexOfFirst { it.fullName == fullName }
            .takeIf { it >= 0 }
            ?.plus(1)

    fun dayIndexFromShortName(shortName: String): Int? =
        weekDays.indexOfFirst { it.shortName == shortName }
            .takeIf { it >= 0 }
            ?.plus(1)

    fun fullNameFromDayIndex(dayIndex: Int): String? =
        weekDays.getOrNull(dayIndex - 1)?.fullName

    fun shortNameFromDayIndex(dayIndex: Int): String? =
        weekDays.getOrNull(dayIndex - 1)?.shortName

    fun shortNamesToDayIndexes(shortNames: Set<String>): List<Int> =
        shortNames.mapNotNull(::dayIndexFromShortName).sorted()

    fun dayIndexesToShortNames(dayIndexes: List<Int>): Set<String> =
        dayIndexes.mapNotNull(::shortNameFromDayIndex).toSet()

    fun defaultDays(): List<StudyPlanDay> = weekDays.map { option ->
        StudyPlanDay(
            name = option.fullName,
            shortName = option.shortName,
            hours = DEFAULT_DAY_HOURS
        )
    }

    fun distributeWeeklyHours(
        weeklyHours: Int,
        unavailableShortNames: Set<String>
    ): List<StudyPlanDay> {
        val available = weekDays.filter { it.shortName !in unavailableShortNames }
        if (available.isEmpty() || weeklyHours <= 0) {
            return weekDays.map { option ->
                StudyPlanDay(
                    name = option.fullName,
                    shortName = option.shortName,
                    hours = 0
                )
            }
        }
        val base = weeklyHours / available.size
        var remainder = weeklyHours % available.size
        return weekDays.map { option ->
            val hours = if (option.shortName in unavailableShortNames) {
                0
            } else {
                val extra = if (remainder > 0) 1 else 0
                if (remainder > 0) remainder--
                base + extra
            }
            StudyPlanDay(
                name = option.fullName,
                shortName = option.shortName,
                hours = hours
            )
        }
    }

    private const val DEFAULT_DAY_HOURS = 2
}

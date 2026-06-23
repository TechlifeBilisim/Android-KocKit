package com.techlife.kockit.feature.studyplan

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object StudyPlanDateFormatter {
    private val turkishLocale = Locale.forLanguageTag("tr")
    private val displayFormatter = SimpleDateFormat("d MMMM yyyy", turkishLocale)

    fun format(millis: Long): String = displayFormatter.format(millis)

    fun formatRange(startMillis: Long, endMillis: Long): String =
        "${format(startMillis)} - ${format(endMillis)}"

    fun startOfDay(millis: Long): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun todayMillis(): Long = startOfDay(System.currentTimeMillis())
}

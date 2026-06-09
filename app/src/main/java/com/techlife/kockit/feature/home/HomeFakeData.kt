package com.techlife.kockit.feature.home

import androidx.compose.ui.graphics.Color
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen

data class HomePriorityLesson(
    val title: String,
    val subtitle: String,
    val completedQuestions: Int,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val accentColor: Color,
    val iconEmoji: String
)

object HomeFakeData {
    const val USER_NAME = "Adem"
    const val NOTIFICATION_COUNT = 3
    const val DAILY_GOAL_COMPLETED = 16
    const val DAILY_GOAL_TOTAL = 40
    const val DAILY_GOAL_REMAINING = 24
    const val GENERAL_PROGRESS = 0.68f
    const val GENERAL_PROGRESS_PERCENT = "%68"
    const val TOTAL_POINTS = "850"
    const val EXAM_AVERAGE_NET = "78 net"

    val priorityLessons = listOf(
        HomePriorityLesson(
            title = "Matematik",
            subtitle = "Problemler",
            completedQuestions = 16,
            totalQuestions = 40,
            durationMinutes = 40,
            accentColor = PastelGreen,
            iconEmoji = "√"
        ),
        HomePriorityLesson(
            title = "Türkçe",
            subtitle = "Paragraf",
            completedQuestions = 12,
            totalQuestions = 30,
            durationMinutes = 30,
            accentColor = LavenderAccent,
            iconEmoji = "📖"
        ),
        HomePriorityLesson(
            title = "Fizik",
            subtitle = "Kuvvet ve Hareket",
            completedQuestions = 8,
            totalQuestions = 25,
            durationMinutes = 25,
            accentColor = OrangeAccent,
            iconEmoji = "⚛"
        )
    )
}

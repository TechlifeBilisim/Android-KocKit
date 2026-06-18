package com.techlife.kockit.feature.placementtest

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.theme.PastelGreen

data class PlacementResultAreaItem(
    val label: String,
    val emoji: String
)

enum class PlacementTestSection(
    val routeKey: String,
    val infoTitle: String,
    val examTitle: String,
    val description: String,
    val questionCount: String,
    val duration: String,
    val durationLabel: String,
    val scopeItems: List<String>,
    val resultScreenTitle: String,
    val correctCount: String,
    val wrongCount: String,
    val emptyCount: String,
    val totalQuestionCount: String,
    val completionTime: String,
    val averageTime: String,
    val strongAreas: List<PlacementResultAreaItem>,
    val weakAreas: List<PlacementResultAreaItem>,
    val accentColor: Color,
    val accentSoftColor: Color,
    val startButtonText: String,
    val nextExamButtonText: String?,
  @DrawableRes val heroIconRes: Int
) {
    GENERAL_ABILITY(
        routeKey = "ability",
        infoTitle = "Genel Yetenek Sınavı",
        examTitle = "Seviye Testi",
        description = "Sayısal, sözel, mantık ve problem çözme becerilerini ölçen sorular seni bekliyor.",
        questionCount = "40",
        duration = "30 dk",
        durationLabel = "Toplam Süre",
        scopeItems = listOf(
            "Sayısal Yetenek",
            "Sözel Yetenek",
            "Mantık",
            "Problem Çözme"
        ),
        resultScreenTitle = "Seviye Sınavı Sonucu",
        correctCount = "31",
        wrongCount = "12",
        emptyCount = "7",
        totalQuestionCount = "50",
        completionTime = "38 dk 24 sn",
        averageTime = "Ortalama: 45 dk",
        strongAreas = listOf(
            PlacementResultAreaItem("Paragraf", "📄"),
            PlacementResultAreaItem("Mantık", "🧠"),
            PlacementResultAreaItem("Fen Bilimleri", "🧪")
        ),
        weakAreas = listOf(
            PlacementResultAreaItem("Tarih", "🏛"),
            PlacementResultAreaItem("Coğrafya", "🌍"),
            PlacementResultAreaItem("Problem Çözme", "🧩")
        ),
        accentColor = PlacementTestColors.orange,
        accentSoftColor = PlacementTestColors.orangeSoft,
        startButtonText = "Sınavı Başlat",
        nextExamButtonText = "Genel Kültür Sınavına Git",
        heroIconRes = R.drawable.img_brain
    ),
    GENERAL_CULTURE(
        routeKey = "culture",
        infoTitle = "Genel Kültür Sınavı",
        examTitle = "Seviye Testi",
        description = "Tarih, coğrafya, fen bilimleri ve güncel bilgilerden oluşan sorular seni bekliyor.",
        questionCount = "40",
        duration = "30 dk",
        durationLabel = "Toplam Süre",
        scopeItems = listOf(
            "Tarih",
            "Coğrafya",
            "Fen Bilimleri",
            "Güncel Bilgiler"
        ),
        resultScreenTitle = "Seviye Sınavı Sonucu",
        correctCount = "28",
        wrongCount = "8",
        emptyCount = "4",
        totalQuestionCount = "40",
        completionTime = "27 dk 10 sn",
        averageTime = "Ortalama: 30 dk",
        strongAreas = listOf(
            PlacementResultAreaItem("Tarih", "🏛"),
            PlacementResultAreaItem("Güncel Bilgiler", "📰")
        ),
        weakAreas = listOf(
            PlacementResultAreaItem("Coğrafya", "🌍"),
            PlacementResultAreaItem("Fen Bilimleri", "🧪")
        ),
        accentColor = PlacementTestColors.purple,
        accentSoftColor = PlacementTestColors.purpleSoft,
        startButtonText = "Sınavı Başlat",
        nextExamButtonText = null,
        heroIconRes = R.drawable.ic_placement_globe
    );

    companion object {
        fun fromRouteKey(key: String): PlacementTestSection =
            entries.first { it.routeKey == key }
    }
}

object PlacementTestColors {
    val orange = Color(0xFFFF8C42)
    val orangeSoft = Color(0xFFFFF0E6)
    val purple = Color(0xFF8E7CC3)
    val purpleSoft = Color(0xFFF3EEFC)
    val green = PastelGreen
    val greenSoft = PastelGreen.copy(alpha = 0.14f)
    val wrongRed = Color(0xFFE85D5D)
    val emptyGray = Color(0xFF9AA0A6)
}

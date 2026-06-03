package com.techlife.kockit.feature.placementtest

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.theme.PastelGreen

enum class PlacementTestSection(
    val routeKey: String,
    val infoTitle: String,
    val examTitle: String,
    val description: String,
    val questionCount: String,
    val duration: String,
    val difficulty: String,
    val scopeItems: List<String>,
    val resultTitle: String,
    val netScore: String,
    val netLabel: String,
    val correctCount: String,
    val wrongCount: String,
    val emptyCount: String,
    val progress: Float,
    val accentColor: Color,
    val accentSoftColor: Color,
    val startButtonText: String,
    val resultButtonText: String,
    @DrawableRes val heroIconRes: Int,
    @DrawableRes val trophyIconRes: Int
) {
    GENERAL_ABILITY(
        routeKey = "ability",
        infoTitle = "Genel Yetenek Sınavı",
        examTitle = "Genel Yetenek",
        description = "Bu sınav, sayısal ve sözel yeteneklerinizi, mantıksal düşünme " +
            "becerilerinizi ve problem çözme kapasitenizi ölçmek için tasarlanmıştır.",
        questionCount = "2",
        duration = "30 dk",
        difficulty = "Orta",
        scopeItems = listOf(
            "Sayısal Yetenek",
            "Sözel Yetenek",
            "Mantık",
            "Problem Çözme"
        ),
        resultTitle = "Genel Yetenek Sınavı Tamamlandı!",
        netScore = "32 / 40",
        netLabel = "Net",
        correctCount = "34",
        wrongCount = "5",
        emptyCount = "1",
        progress = 0.8f,
        accentColor = PlacementTestColors.orange,
        accentSoftColor = PlacementTestColors.orangeSoft,
        startButtonText = "Sınavı Başlat",
        resultButtonText = "Devam Et",
        heroIconRes = R.drawable.img_brain,
        trophyIconRes = R.drawable.ic_placement_trophy_gold
    ),
    GENERAL_CULTURE(
        routeKey = "culture",
        infoTitle = "Genel Kültür Sınavı",
        examTitle = "Genel Kültür",
        description = "Bu sınav, tarih, coğrafya, vatandaşlık, fen bilimleri ve güncel " +
            "olaylar konularındaki bilgi düzeyinizi ölçmek için hazırlanmıştır.",
        questionCount = "2",
        duration = "40 dk",
        difficulty = "Orta",
        scopeItems = listOf(
            "Tarih",
            "Coğrafya",
            "Vatandaşlık",
            "Fen Bilimleri",
            "Güncel Bilgiler"
        ),
        resultTitle = "Genel Kültür Sınavı Tamamlandı!",
        netScore = "28 / 40",
        netLabel = "Net",
        correctCount = "30",
        wrongCount = "8",
        emptyCount = "2",
        progress = 0.7f,
        accentColor = PlacementTestColors.purple,
        accentSoftColor = PlacementTestColors.purpleSoft,
        startButtonText = "Sınavı Başlat",
        resultButtonText = "Sonuçları Analiz Et",
        heroIconRes = R.drawable.ic_placement_globe,
        trophyIconRes = R.drawable.ic_placement_trophy_purple
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
    val emptyBlue = Color(0xFF5B8DEF)
}

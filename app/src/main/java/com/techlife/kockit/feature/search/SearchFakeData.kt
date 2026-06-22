package com.techlife.kockit.feature.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Science
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SearchPopularTopic(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBg: Color,
    val cardBg: Color,
    val iconTint: Color
)

object SearchFakeData {
    val recentSearches = listOf(
        "Problemler",
        "Paragraf",
        "TYT Matematik",
        "Fonksiyonlar",
        "Deneme Sınavı"
    )

    val popularTopics = listOf(
        SearchPopularTopic(
            title = "Matematik",
            subtitle = "Problemler, Fonksiyonlar, Türev, İntegral, Limit",
            icon = Icons.Outlined.Science,
            iconBg = Color(0xFFD8F0E3),
            cardBg = Color(0xFFF3FBF6),
            iconTint = Color(0xFF4DB6AC)
        ),
        SearchPopularTopic(
            title = "Türkçe",
            subtitle = "Paragraf, Dil Bilgisi, Anlam Bilgisi, Yazım Kuralları",
            icon = Icons.Outlined.Description,
            iconBg = Color(0xFFE8E2F5),
            cardBg = Color(0xFFF6F3FC),
            iconTint = Color(0xFF9B8FD9)
        ),
        SearchPopularTopic(
            title = "Fizik",
            subtitle = "Kuvvet, Hareket, Enerji, Elektrik, Manyetizma",
            icon = Icons.Outlined.Science,
            iconBg = Color(0xFFFFE8D6),
            cardBg = Color(0xFFFFF5EE),
            iconTint = Color(0xFFFF8C42)
        ),
        SearchPopularTopic(
            title = "Kimya",
            subtitle = "Asit-Baz, Mol, Kimyasal Tepkimeler, Organik Kimya",
            icon = Icons.Outlined.Science,
            iconBg = Color(0xFFD9F2EE),
            cardBg = Color(0xFFF2FAF8),
            iconTint = Color(0xFF5DB7A6)
        ),
        SearchPopularTopic(
            title = "Tarih",
            subtitle = "Osmanlı, Cumhuriyet, İnkılap Tarihi, Çağdaş Türk",
            icon = Icons.Outlined.Public,
            iconBg = Color(0xFFE4E6F8),
            cardBg = Color(0xFFF5F6FD),
            iconTint = Color(0xFF7B83C8)
        ),
        SearchPopularTopic(
            title = "Biyoloji",
            subtitle = "Hücre, Ekosistem, Genetik, İnsan Fizyolojisi",
            icon = Icons.Outlined.Eco,
            iconBg = Color(0xFFFFE9DC),
            cardBg = Color(0xFFFFF7F2),
            iconTint = Color(0xFFE89B6C)
        )
    )
}

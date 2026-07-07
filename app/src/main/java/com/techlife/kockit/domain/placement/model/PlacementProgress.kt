package com.techlife.kockit.domain.placement.model

data class PlacementProgress(
    val isGeneralAbilityCompleted: Boolean = false,
    val isGeneralCultureCompleted: Boolean = false
) {
    val shouldShowReminderCard: Boolean
        get() = !isGeneralAbilityCompleted || !isGeneralCultureCompleted

    val remainingCount: Int
        get() = listOf(isGeneralAbilityCompleted, isGeneralCultureCompleted).count { !it }

    val nextIncompleteSectionKey: String?
        get() = when {
            !isGeneralAbilityCompleted -> SECTION_GENERAL_ABILITY
            !isGeneralCultureCompleted -> SECTION_GENERAL_CULTURE
            else -> null
        }

    companion object {
        const val SECTION_GENERAL_ABILITY = "ability"
        const val SECTION_GENERAL_CULTURE = "culture"
    }
}

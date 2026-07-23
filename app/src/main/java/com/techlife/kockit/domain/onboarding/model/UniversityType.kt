package com.techlife.kockit.domain.onboarding.model

/**
 * Backend [UnversiteTurId] ile eşleşir: Devlet=1, Vakıf=2, Diğer=3.
 */
enum class UniversityType(val label: String, val id: Int) {
    DEVLET("Devlet", 1),
    VAKIF("Vakıf", 2),
    OZEL("Diğer", 3)
}

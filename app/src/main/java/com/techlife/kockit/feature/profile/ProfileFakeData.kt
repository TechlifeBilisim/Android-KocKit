package com.techlife.kockit.feature.profile

data class ProfileStudyDetail(
    val title: String,
    val value: String
)

object ProfileFakeData {
    const val NOTIFICATION_COUNT = 5
    const val FULL_NAME = "Adem POLAT"
    const val GRADE = "12. Sınıf"
    const val EXAM_TYPE = "TYT + AYT"
    const val LOCATION = "İstanbul / Sultangazi"
    const val SCHOOL = "Küçükköy Anadolu Teknik Lisesi"
    const val LEVEL_LABEL = "Başarılı"

    const val TARGET_RANK = "İlk 1.000"
    const val CURRENT_RANK = "3.420"
    const val TARGET_PROGRESS = 0.68f
    const val TARGET_PROGRESS_TEXT = "%68 Tamamlandı"

    const val TOTAL_POINTS = "850"
    const val POINTS_PERIOD = "Bu Ay"

    const val WEEKLY_STUDY_HOURS = "35 Saat"
    const val WEEKLY_STUDY_PROGRESS = 0.70f
    const val WEEKLY_STUDY_PERCENT = "%70"

    val studyDetails = listOf(
        ProfileStudyDetail("Oturum Süresi", "50 dk"),
        ProfileStudyDetail("Genel Tekrar Günü", "Pazar"),
        ProfileStudyDetail("Paragraf Seansı", "30 dk"),
        ProfileStudyDetail("Problem Seansı", "45 dk")
    )

    const val PREP_PHASE = "Evre 3"
    const val PREP_PHASE_SUBTITLE = "Soru Çözüm Dönemi"
    const val PREP_PROGRESS = 0.72f
    const val PREP_PROGRESS_TEXT = "%72 Tamamlandı"

    val unavailableDays = listOf("Cumartesi", "Pazar")
    const val UNAVAILABLE_DAYS_NOTE =
        "Belirtilen günlerde çalışma programı önerileri yapılmayacaktır."
}

package com.techlife.kockit.data.onboarding.local

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeOnboardingDataSource @Inject constructor() {

    fun getExamGoals(): List<ExamGoal> = listOf(
        ExamGoal(id = "tyt", title = "TYT", subtitle = "Temel Yeterlilik Testi", type = "TYT"),
        ExamGoal(id = "ayt", title = "AYT", subtitle = "Alan Yeterlilik Testi", type = "AYT"),
        ExamGoal(id = "yks", title = "YKS", subtitle = "TYT + AYT", type = "YKS")
    )

    fun getUniversities(): List<University> = listOf(
        University("1", "Boğaziçi University"),
        University("2", "Istanbul Technical University"),
        University("3", "Middle East Technical University"),
        University("4", "Istanbul University"),
        University("5", "Ankara University"),
        University("6", "Hacettepe University"),
        University("7", "Yıldız Technical University"),
        University("8", "Marmara University"),
        University("9", "Ege University"),
        University("10", "Dokuz Eylül University")
    )

    fun getDepartments(): List<Department> = listOf(
        Department("1", "Computer Engineering"),
        Department("2", "Software Engineering"),
        Department("3", "Medicine"),
        Department("4", "Law"),
        Department("5", "Psychology"),
        Department("6", "Electrical and Electronics Engineering"),
        Department("7", "Industrial Engineering"),
        Department("8", "Architecture"),
        Department("9", "Business Administration"),
        Department("10", "Dentistry")
    )
}

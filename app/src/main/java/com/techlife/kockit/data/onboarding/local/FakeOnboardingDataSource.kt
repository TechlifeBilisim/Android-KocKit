package com.techlife.kockit.data.onboarding.local

import com.techlife.kockit.domain.onboarding.model.Department
import com.techlife.kockit.domain.onboarding.model.ExamGoal
import com.techlife.kockit.domain.onboarding.model.University
import com.techlife.kockit.feature.goalsetup.GoalSetupDepartmentCatalog
import com.techlife.kockit.feature.goalsetup.GoalSetupUniversityCatalog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeOnboardingDataSource @Inject constructor() {

    fun getExamGoals(): List<ExamGoal> = listOf(
        ExamGoal(id = "tyt", title = "TYT", subtitle = "Temel Yeterlilik Testi", type = "TYT"),
        ExamGoal(id = "ayt", title = "AYT", subtitle = "Alan Yeterlilik Testleri", type = "AYT")
    )

    fun getUniversities(): List<University> = GoalSetupUniversityCatalog.universities

    fun getDepartments(): List<Department> = GoalSetupDepartmentCatalog.departments
}

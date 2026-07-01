package com.techlife.kockit.feature.profilegoals

import com.techlife.kockit.feature.goalsetup.GoalSetupOption

object ProfileGoalOptions {
    val studyTimeOptions = listOf(
        GoalSetupOption(id = "1h", label = "1 Saat"),
        GoalSetupOption(id = "2h", label = "2 Saat"),
        GoalSetupOption(id = "3h", label = "3 Saat"),
        GoalSetupOption(id = "4h_plus", label = "4+ Saat")
    )

    val rankGoalOptions = listOf(
        GoalSetupOption(id = "50000", label = "İlk 50.000"),
        GoalSetupOption(id = "20000", label = "İlk 20.000"),
        GoalSetupOption(id = "10000", label = "İlk 10.000"),
        GoalSetupOption(id = "1000", label = "İlk 1.000")
    )
}

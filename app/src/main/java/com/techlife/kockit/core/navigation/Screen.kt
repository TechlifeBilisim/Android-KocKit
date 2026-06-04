package com.techlife.kockit.core.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object GoalSetup : Screen("goal_setup")
    data object Placement : Screen("placement")
    data object Main : Screen("main")
    data object Home : Screen("home")

    companion object {
        const val PLACEMENT_SECTION_ARG = "section"
        const val PLACEMENT_INFO_ROUTE = "placement/{section}/info"
        const val PLACEMENT_EXAM_ROUTE = "placement/{section}/exam"
        const val PLACEMENT_RESULT_ROUTE = "placement/{section}/result"

        fun placementInfo(sectionKey: String) = "placement/$sectionKey/info"
        fun placementExam(sectionKey: String) = "placement/$sectionKey/exam"
        fun placementResult(sectionKey: String) = "placement/$sectionKey/result"
    }
}

package com.techlife.kockit.feature.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.layout.ProfileContentContainer
import com.techlife.kockit.core.designsystem.layout.rememberProfileLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CreamBackground
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.feature.editprofile.EditProfileScreen
import com.techlife.kockit.feature.profilegoals.ProfileGoalsFlowScreen

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var showGoalsFlow by rememberSaveable { mutableStateOf(false) }
    var showEditProfile by rememberSaveable { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showMessage: (String) -> Unit = { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    if (showEditProfile) {
        EditProfileScreen(
            onBackClick = { showEditProfile = false },
            onSaved = {
                showEditProfile = false
                viewModel.refresh()
            },
            onShowMessage = showMessage
        )
        return
    }

    if (showGoalsFlow) {
        ProfileGoalsFlowScreen(
            onExit = { showGoalsFlow = false },
            onComplete = { showGoalsFlow = false },
            onShowMessage = showMessage
        )
        return
    }
    val metrics = rememberProfileLayoutMetrics()

    ProfileContentContainer(metrics = metrics) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@ProfileContentContainer
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground),
            verticalArrangement = Arrangement.spacedBy(metrics.sectionSpacing)
        ) {
            item(key = "top_bar") {
                Spacer(modifier = Modifier.height(metrics.topInset))
                ProfileTopBar(onBackClick = onBackClick)
            }
            item(key = "header") {
                ProfileHeaderSection()
            }
            uiState.errorMessage?.let { message ->
                item(key = "error") {
                    KocKitText(text = message, color = OrangeAccent)
                }
            }
            item(key = "summary") {
                ProfileSummaryCard(
                    fullName = uiState.fullName.ifBlank { "Profil" },
                    grade = uiState.grade,
                    examType = uiState.examType,
                    location = uiState.location,
                    school = uiState.school,
                    levelLabel = uiState.levelLabel,
                    profileImage = uiState.profileImage
                )
            }
            item(key = "goals") {
                ProfileGoalsSection(
                    targetRank = ProfileFakeData.TARGET_RANK,
                    currentRank = ProfileFakeData.CURRENT_RANK,
                    targetProgress = ProfileFakeData.TARGET_PROGRESS,
                    targetProgressText = ProfileFakeData.TARGET_PROGRESS_TEXT,
                    totalPoints = ProfileFakeData.TOTAL_POINTS,
                    pointsPeriod = ProfileFakeData.POINTS_PERIOD
                )
            }
            item(key = "study_program") {
                ProfileStudyProgramCard(
                    weeklyHours = uiState.weeklyStudyHours,
                    weeklyProgress = uiState.weeklyStudyProgress,
                    weeklyPercent = uiState.weeklyStudyPercent,
                    details = ProfileFakeData.studyDetails,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item(key = "account_settings") {
                ProfileAccountSettingsCard(
                    onPersonalInfoClick = { showEditProfile = true },
                    onGoalsClick = { showGoalsFlow = true },
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 840, heightDp = 1200, name = "Tablet")
@Composable
fun ProfileScreenTabletPreview() {
    KocKitTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KocKitTheme {
        ProfileScreen()
    }
}

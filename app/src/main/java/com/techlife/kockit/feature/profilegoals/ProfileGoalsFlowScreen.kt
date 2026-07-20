package com.techlife.kockit.feature.profilegoals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.R
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitGoalMotivationCard
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitSimpleSelectableCard
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.feature.goalsetup.GoalSetupOption
import kotlinx.coroutines.flow.collectLatest

private const val STEP_STUDY_TIME = 1
private const val STEP_RANK_GOAL = 2

@Composable
fun ProfileGoalsFlowScreen(
    onExit: () -> Unit,
    onComplete: () -> Unit,
    onShowMessage: (String) -> Unit = {},
    viewModel: ProfileGoalsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by rememberSaveable { mutableIntStateOf(STEP_STUDY_TIME) }
    var selectedStudyTimeId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedRankGoalId by rememberSaveable { mutableStateOf<String?>(null) }
    var studyTimeError by rememberSaveable { mutableStateOf<String?>(null) }
    var rankGoalError by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileGoalsEffect.Completed -> onComplete()
                is ProfileGoalsEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        KocKitBackground(useFormBackgroundImage = true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                KocKitTopBar(
                    onBackClick = {
                        if (currentStep == STEP_RANK_GOAL) {
                            currentStep = STEP_STUDY_TIME
                        } else {
                            onExit()
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (currentStep) {
                        STEP_STUDY_TIME -> ProfileStudyTimeStep(
                            options = ProfileGoalOptions.studyTimeOptions,
                            selectedId = selectedStudyTimeId,
                            onSelect = {
                                selectedStudyTimeId = it
                                studyTimeError = null
                            },
                            error = studyTimeError
                        )
                        STEP_RANK_GOAL -> ProfileRankGoalStep(
                            options = ProfileGoalOptions.rankGoalOptions,
                            selectedId = selectedRankGoalId,
                            onSelect = {
                                selectedRankGoalId = it
                                rankGoalError = null
                                viewModel.onRankGoalSelected(it)
                            },
                            uiState = uiState,
                            onPuanTurSelected = viewModel::onPuanTurSelected,
                            onPuanInputChanged = viewModel::onPuanInputChanged,
                            onOkulPuanInputChanged = viewModel::onOkulPuanInputChanged,
                            onCalculateFromPuan = viewModel::onCalculateSiralamaFromPuan,
                            error = rankGoalError ?: uiState.errorMessage
                        )
                    }
                }
                KocKitPrimaryButton(
                    text = if (currentStep == STEP_RANK_GOAL) "Kaydet" else "Devam Et",
                    onClick = {
                        when (currentStep) {
                            STEP_STUDY_TIME -> {
                                if (selectedStudyTimeId == null) {
                                    studyTimeError = "Çalışma süresi seçimi gerekli"
                                } else {
                                    currentStep = STEP_RANK_GOAL
                                }
                            }
                            STEP_RANK_GOAL -> {
                                val studyId = selectedStudyTimeId
                                val rankId = selectedRankGoalId
                                if (rankId == null) {
                                    rankGoalError = "Hedef seçimi gerekli"
                                } else if (studyId != null) {
                                    viewModel.submit(studyId, rankId)
                                }
                            }
                        }
                    },
                    enabled = !uiState.isLoading,
                    isLoading = uiState.isLoading,
                    showTrailingArrow = false,
                    containerColor = OrangeAccent,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileStudyTimeStep(
    options: List<GoalSetupOption>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    error: String?
) {
    ProfileGoalStepHeader(
        title = "Çalışma Süreni Belirle",
        subtitle = "Günlük çalışma süreni seç, sana özel plan oluşturalım."
    )
    Spacer(modifier = Modifier.height(4.dp))
    options.forEach { option ->
        KocKitSimpleSelectableCard(
            label = option.label,
            isSelected = selectedId == option.id,
            onClick = { onSelect(option.id) }
        )
    }
    if (selectedId != null) {
        KocKitGoalMotivationCard(
            message = "Düzenli çalışma, hedefe ulaşmanın anahtarıdır!",
            iconResId = R.drawable.ic_goal_study
        )
    }
    error?.let {
        KocKitText(text = it, color = OrangeAccent)
    }
}

@Composable
private fun ProfileRankGoalStep(
    options: List<GoalSetupOption>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    uiState: ProfileGoalsUiState,
    onPuanTurSelected: (Int) -> Unit,
    onPuanInputChanged: (String) -> Unit,
    onOkulPuanInputChanged: (String) -> Unit,
    onCalculateFromPuan: () -> Unit,
    error: String?
) {
    ProfileGoalStepHeader(
        title = "Hedefini Belirle",
        subtitle = "Hedef sıralamanı seç veya puanından sıralama hesapla."
    )
    Spacer(modifier = Modifier.height(4.dp))

    val selectedPuanTurLabel = ProfileGoalOptions.puanTurOptions
        .find { it.id == uiState.selectedPuanTurId.toString() }
        ?.label
    KocKitDropdownField(
        label = "Puan Türü",
        options = ProfileGoalOptions.puanTurOptions.map { it.label },
        selectedOption = selectedPuanTurLabel,
        onOptionSelected = { label ->
            val option = ProfileGoalOptions.puanTurOptions.find { it.label == label } ?: return@KocKitDropdownField
            onPuanTurSelected(option.id.toInt())
        }
    )

    options.forEach { option ->
        KocKitSimpleSelectableCard(
            label = option.label,
            isSelected = selectedId == option.id,
            onClick = { onSelect(option.id) }
        )
    }

    if (uiState.isConversionLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PastelGreen)
        }
    }

    uiState.conversionMessage?.let { message ->
        KocKitGoalMotivationCard(
            message = message,
            iconResId = R.drawable.img_target
        )
    }

    if (selectedId != null && uiState.conversionMessage == null && !uiState.isConversionLoading) {
        KocKitGoalMotivationCard(
            message = "Büyük hedefler, planlı adımlarla gerçekleşir!",
            iconResId = R.drawable.img_target
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    KocKitSemiText(
        text = "veya puanından sıralama hesapla",
        color = TextSecondary,
        fontSize = KocKitTextDefaults.fontSizeBody,
        lineHeight = KocKitTextDefaults.lineHeightBody
    )
    KocKitTextField(
        value = uiState.puanInput,
        onValueChange = onPuanInputChanged,
        placeholder = "Puan (örn. 458)",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
    KocKitTextField(
        value = uiState.okulPuanInput,
        onValueChange = onOkulPuanInputChanged,
        placeholder = "Okul puanı (örn. 85)",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
    KocKitPrimaryButton(
        text = "Sıralama Hesapla",
        onClick = onCalculateFromPuan,
        enabled = !uiState.isConversionLoading,
        isLoading = false,
        showTrailingArrow = false,
        containerColor = PastelGreen,
        height = 48.dp,
        fontSize = KocKitTextDefaults.fontSizeBodyLarge
    )

    error?.let {
        KocKitText(text = it, color = OrangeAccent)
    }
}

@Composable
private fun ProfileGoalStepHeader(
    title: String,
    subtitle: String
) {
    KocKitExtraBoldText(
        text = title,
        fontSize = KocKitTextDefaults.fontSizeHeadline,
        lineHeight = KocKitTextDefaults.lineHeightHeadline
    )
    Spacer(modifier = Modifier.height(8.dp))
    KocKitSemiText(
        text = subtitle,
        color = TextSecondary,
        fontSize = KocKitTextDefaults.fontSizeBodyLarge,
        lineHeight = KocKitTextDefaults.lineHeightBodyLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileGoalsFlowScreenPreview() {
    KocKitTheme {
        // Preview without ViewModel wiring
    }
}

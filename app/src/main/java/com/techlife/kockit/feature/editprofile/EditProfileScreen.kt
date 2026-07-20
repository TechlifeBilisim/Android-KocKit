package com.techlife.kockit.feature.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techlife.kockit.core.designsystem.background.KocKitBackground
import com.techlife.kockit.core.designsystem.component.KocKitDropdownField
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitProfileAvatar
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextDefaults
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.component.KocKitTopBar
import com.techlife.kockit.core.designsystem.theme.ErrorAccent
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.domain.onboarding.model.Gender
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit = onBackClick,
    onShowMessage: (String) -> Unit = {},
    viewModel: EditProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvent(EditProfileEvent.ImageSelected(uri))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                EditProfileEffect.Saved -> onSaved()
                is EditProfileEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    EditProfileContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onPickImageClick = {
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        modifier = modifier
    )
}

@Composable
private fun EditProfileContent(
    uiState: EditProfileUiState,
    onEvent: (EditProfileEvent) -> Unit,
    onBackClick: () -> Unit,
    onPickImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        KocKitBackground(useFormBackgroundImage = true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                KocKitTopBar(onBackClick = onBackClick)
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        KocKitSemiText(
                            text = "Profili Düzenle",
                            color = TextPrimary,
                            fontSize = KocKitTextDefaults.fontSizeTitle,
                            lineHeight = KocKitTextDefaults.lineHeightTitle,
                            modifier = Modifier.fillMaxWidth()
                        )
                        KocKitText(
                            text = "Kişisel bilgilerini ve profil fotoğrafını güncelle.",
                            color = TextSecondary,
                            fontSize = KocKitTextDefaults.fontSizeBody,
                            lineHeight = KocKitTextDefaults.lineHeightBody,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(contentAlignment = Alignment.Center) {
                            KocKitProfileAvatar(
                                imageSource = uiState.profileImagePreview,
                                size = 104.dp,
                                showCameraBadge = true,
                                onClick = onPickImageClick
                            )
                            if (uiState.isEncodingImage) {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        KocKitText(
                            text = "Profil fotoğrafı değiştir",
                            color = TextSecondary,
                            fontSize = KocKitTextDefaults.fontSizeSmall,
                            lineHeight = KocKitTextDefaults.lineHeightSmall,
                            modifier = Modifier.clickable(onClick = onPickImageClick)
                        )
                        KocKitTextField(
                            value = uiState.ad,
                            onValueChange = { onEvent(EditProfileEvent.AdChanged(it)) },
                            placeholder = "Ad",
                            error = uiState.adError
                        )
                        KocKitTextField(
                            value = uiState.soyad,
                            onValueChange = { onEvent(EditProfileEvent.SoyadChanged(it)) },
                            placeholder = "Soyad",
                            error = uiState.soyadError
                        )
                        KocKitTextField(
                            value = uiState.rumuz,
                            onValueChange = { onEvent(EditProfileEvent.RumuzChanged(it)) },
                            placeholder = "Rumuz",
                            error = uiState.rumuzError
                        )
                        KocKitTextField(
                            value = uiState.eposta,
                            onValueChange = { onEvent(EditProfileEvent.EpostaChanged(it)) },
                            placeholder = "E-posta",
                            error = uiState.epostaError
                        )
                        GenderSwitch(
                            selectedGender = uiState.selectedGender,
                            onGenderSelected = { onEvent(EditProfileEvent.GenderSelected(it)) }
                        )
                        FieldError(uiState.genderError)
                        KocKitDropdownField(
                            label = "İl",
                            options = uiState.provinces.map { it.name },
                            selectedOption = uiState.selectedProvinceName,
                            onOptionSelected = { name ->
                                val province = uiState.provinces.find { it.name == name }
                                    ?: return@KocKitDropdownField
                                onEvent(
                                    EditProfileEvent.ProvinceSelected(province.id, province.name)
                                )
                            },
                            error = uiState.provinceError ?: uiState.provincesError,
                            searchable = true,
                            searchPlaceholder = if (uiState.isProvincesLoading) {
                                "İller yükleniyor..."
                            } else {
                                "İl ara..."
                            }
                        )
                        KocKitDropdownField(
                            label = "İlçe",
                            options = uiState.districts.map { it.name },
                            selectedOption = uiState.selectedDistrictName,
                            onOptionSelected = { name ->
                                val district = uiState.districts.find { it.name == name }
                                    ?: return@KocKitDropdownField
                                onEvent(
                                    EditProfileEvent.DistrictSelected(district.id, district.name)
                                )
                            },
                            error = uiState.districtError ?: uiState.districtsError,
                            searchable = true,
                            searchPlaceholder = when {
                                uiState.isDistrictsLoading -> "İlçeler yükleniyor..."
                                uiState.selectedProvinceId == null -> "Önce il seçin"
                                else -> "İlçe ara..."
                            }
                        )
                        val selectedTemaLabel = EditProfileTemaOptions.options
                            .find { it.first == uiState.selectedTemaId }
                            ?.second
                        KocKitDropdownField(
                            label = "Uygulama Teması",
                            options = EditProfileTemaOptions.options.map { it.second },
                            selectedOption = selectedTemaLabel,
                            onOptionSelected = { label ->
                                val tema = EditProfileTemaOptions.options
                                    .find { it.second == label }
                                    ?: return@KocKitDropdownField
                                onEvent(EditProfileEvent.TemaSelected(tema.first))
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        KocKitPrimaryButton(
                            text = "Kaydet",
                            onClick = { onEvent(EditProfileEvent.SaveClick) },
                            isLoading = uiState.isSaving,
                            enabled = !uiState.isSaving && !uiState.isEncodingImage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenderSwitch(
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Gender.entries.forEach { gender ->
            val isSelected = selectedGender == gender
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) LavenderAccent else Color.Transparent)
                    .clickable { onGenderSelected(gender) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                KocKitSemiText(
                    text = gender.label,
                    color = if (isSelected) Color.White else TextPrimary,
                    fontSize = KocKitTextDefaults.fontSizeBody,
                    lineHeight = KocKitTextDefaults.lineHeightBody
                )
            }
        }
    }
}

@Composable
private fun FieldError(message: String?) {
    if (message.isNullOrBlank()) return
    KocKitText(
        text = message,
        color = ErrorAccent,
        fontSize = KocKitTextDefaults.fontSizeSmall,
        lineHeight = KocKitTextDefaults.lineHeightSmall,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun EditProfileContentPreview() {
    KocKitTheme {
        EditProfileContent(
            uiState = EditProfileUiState(
                ad = "Adem",
                soyad = "Polat",
                rumuz = "adeee",
                eposta = "adem@example.com",
                selectedGender = Gender.ERKEK
            ),
            onEvent = {},
            onBackClick = {},
            onPickImageClick = {}
        )
    }
}

package com.techlife.kockit.feature.studyplan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.component.KocKitTextField
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.PrimaryTeal
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White
import java.util.UUID

private enum class DatePickerTarget {
    SINGLE,
    RANGE_START,
    RANGE_END
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanAddSpecialDateSheet(
    onDismiss: () -> Unit,
    onAdd: (StudyPlanSpecialDate) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf(StudyPlanSpecialDateMode.SINGLE) }
    var singleDateMillis by remember { mutableStateOf<Long?>(null) }
    var rangeStartMillis by remember { mutableStateOf<Long?>(null) }
    var rangeEndMillis by remember { mutableStateOf<Long?>(null) }
    var note by remember { mutableStateOf("") }
    var pickerTarget by remember { mutableStateOf<DatePickerTarget?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val initialPickerMillis = remember(pickerTarget, singleDateMillis, rangeStartMillis, rangeEndMillis) {
        when (pickerTarget) {
            DatePickerTarget.SINGLE -> singleDateMillis
            DatePickerTarget.RANGE_START -> rangeStartMillis
            DatePickerTarget.RANGE_END -> rangeEndMillis
            null -> null
        }
    }

    if (pickerTarget != null) {
        val target = pickerTarget!!
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialPickerMillis ?: StudyPlanDateFormatter.todayMillis()
        )
        DatePickerDialog(
            onDismissRequest = { pickerTarget = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val normalized = StudyPlanDateFormatter.startOfDay(selectedMillis)
                            when (target) {
                                DatePickerTarget.SINGLE -> singleDateMillis = normalized
                                DatePickerTarget.RANGE_START -> {
                                    rangeStartMillis = normalized
                                    if (rangeEndMillis != null && rangeEndMillis!! < normalized) {
                                        rangeEndMillis = normalized
                                    }
                                }
                                DatePickerTarget.RANGE_END -> rangeEndMillis = normalized
                            }
                            errorMessage = null
                        }
                        pickerTarget = null
                    }
                ) {
                    KocKitSemiText(text = "Tamam", color = PrimaryTeal)
                }
            },
            dismissButton = {
                TextButton(onClick = { pickerTarget = null }) {
                    KocKitSemiText(text = "İptal", color = TextSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        StudyPlanAddSpecialDateContent(
            mode = mode,
            singleDateMillis = singleDateMillis,
            rangeStartMillis = rangeStartMillis,
            rangeEndMillis = rangeEndMillis,
            note = note,
            errorMessage = errorMessage,
            onModeSelected = {
                mode = it
                errorMessage = null
            },
            onSingleDateClick = { pickerTarget = DatePickerTarget.SINGLE },
            onRangeStartClick = { pickerTarget = DatePickerTarget.RANGE_START },
            onRangeEndClick = { pickerTarget = DatePickerTarget.RANGE_END },
            onNoteChange = { note = it },
            onAddClick = {
                val result = buildSpecialDate(
                    mode = mode,
                    singleDateMillis = singleDateMillis,
                    rangeStartMillis = rangeStartMillis,
                    rangeEndMillis = rangeEndMillis,
                    note = note.trim()
                )
                result.onSuccess { onAdd(it) }
                    .onFailure { errorMessage = it.message }
            }
        )
    }
}

@Composable
private fun StudyPlanAddSpecialDateContent(
    mode: StudyPlanSpecialDateMode,
    singleDateMillis: Long?,
    rangeStartMillis: Long?,
    rangeEndMillis: Long?,
    note: String,
    errorMessage: String?,
    onModeSelected: (StudyPlanSpecialDateMode) -> Unit,
    onSingleDateClick: () -> Unit,
    onRangeStartClick: () -> Unit,
    onRangeEndClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        KocKitBoldText(
            text = "Özel Tarih Ekle",
            color = TextPrimary,
            fontSize = 18.sp,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyPlanDateModeSelector(
            selectedMode = mode,
            onModeSelected = onModeSelected
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (mode) {
            StudyPlanSpecialDateMode.SINGLE -> {
                StudyPlanDateSelectField(
                    label = "Tarih",
                    value = singleDateMillis?.let(StudyPlanDateFormatter::format),
                    placeholder = "Tarih seçin",
                    onClick = onSingleDateClick
                )
            }
            StudyPlanSpecialDateMode.RANGE -> {
                StudyPlanDateSelectField(
                    label = "Başlangıç",
                    value = rangeStartMillis?.let(StudyPlanDateFormatter::format),
                    placeholder = "Başlangıç tarihi",
                    onClick = onRangeStartClick
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudyPlanDateSelectField(
                    label = "Bitiş",
                    value = rangeEndMillis?.let(StudyPlanDateFormatter::format),
                    placeholder = "Bitiş tarihi",
                    onClick = onRangeEndClick
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        KocKitTextField(
            value = note,
            onValueChange = onNoteChange,
            placeholder = "Açıklama (isteğe bağlı)",
            fieldHeight = 54.dp
        )
        errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            KocKitText(
                text = message,
                color = com.techlife.kockit.core.designsystem.theme.ErrorAccent,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        KocKitPrimaryButton(
            text = "Ekle",
            onClick = onAddClick
        )
    }
}

@Composable
private fun StudyPlanDateModeSelector(
    selectedMode: StudyPlanSpecialDateMode,
    onModeSelected: (StudyPlanSpecialDateMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StudyPlanDateModeChip(
            text = "Tek Tarih",
            selected = selectedMode == StudyPlanSpecialDateMode.SINGLE,
            onClick = { onModeSelected(StudyPlanSpecialDateMode.SINGLE) },
            modifier = Modifier.weight(1f)
        )
        StudyPlanDateModeChip(
            text = "Tarih Aralığı",
            selected = selectedMode == StudyPlanSpecialDateMode.RANGE,
            onClick = { onModeSelected(StudyPlanSpecialDateMode.RANGE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StudyPlanDateModeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PrimaryTeal.copy(alpha = 0.12f) else White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) PrimaryTeal else TextSecondary.copy(alpha = 0.25f)
        )
    ) {
        KocKitSemiText(
            text = text,
            color = if (selected) PrimaryTeal else TextSecondary,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun StudyPlanDateSelectField(
    label: String,
    value: String?,
    placeholder: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        KocKitSemiText(
            text = label,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 13.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = White,
            border = BorderStroke(1.dp, TextSecondary.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = PrimaryTeal,
                    modifier = Modifier.padding(end = 10.dp)
                )
                KocKitSemiText(
                    text = value ?: placeholder,
                    color = if (value != null) TextPrimary else TextSecondary.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun buildSpecialDate(
    mode: StudyPlanSpecialDateMode,
    singleDateMillis: Long?,
    rangeStartMillis: Long?,
    rangeEndMillis: Long?,
    note: String
): Result<StudyPlanSpecialDate> {
    val title = when (mode) {
        StudyPlanSpecialDateMode.SINGLE -> {
            val millis = singleDateMillis
                ?: return Result.failure(IllegalArgumentException("Lütfen bir tarih seçin."))
            StudyPlanDateFormatter.format(millis)
        }
        StudyPlanSpecialDateMode.RANGE -> {
            val start = rangeStartMillis
                ?: return Result.failure(IllegalArgumentException("Lütfen başlangıç tarihini seçin."))
            val end = rangeEndMillis
                ?: return Result.failure(IllegalArgumentException("Lütfen bitiş tarihini seçin."))
            if (end < start) {
                return Result.failure(IllegalArgumentException("Bitiş tarihi başlangıçtan önce olamaz."))
            }
            StudyPlanDateFormatter.formatRange(start, end)
        }
    }
    return Result.success(
        StudyPlanSpecialDate(
            id = UUID.randomUUID().toString(),
            title = title,
            subtitle = note
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun StudyPlanAddSpecialDateContentPreview() {
    KocKitTheme {
        Surface(color = White) {
            StudyPlanAddSpecialDateContent(
                mode = StudyPlanSpecialDateMode.SINGLE,
                singleDateMillis = null,
                rangeStartMillis = null,
                rangeEndMillis = null,
                note = "",
                errorMessage = null,
                onModeSelected = {},
                onSingleDateClick = {},
                onRangeStartClick = {},
                onRangeEndClick = {},
                onNoteChange = {},
                onAddClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudyPlanAddSpecialDateContentRangePreview() {
    KocKitTheme {
        Surface(color = White) {
            StudyPlanAddSpecialDateContent(
                mode = StudyPlanSpecialDateMode.RANGE,
                singleDateMillis = null,
                rangeStartMillis = 1718880000000L, // Example millis
                rangeEndMillis = 1719744000000L, // Example millis
                note = "Yaz tatili",
                errorMessage = null,
                onModeSelected = {},
                onSingleDateClick = {},
                onRangeStartClick = {},
                onRangeEndClick = {},
                onNoteChange = {},
                onAddClick = {}
            )
        }
    }
}

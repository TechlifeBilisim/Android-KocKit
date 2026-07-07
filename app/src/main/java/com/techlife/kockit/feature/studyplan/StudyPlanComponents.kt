package com.techlife.kockit.feature.studyplan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.component.KocKitBackButton
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitNumberPickerField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.layout.LocalStudyPlanLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.CoralAccent
import com.techlife.kockit.core.designsystem.theme.ErrorAccent
import com.techlife.kockit.core.designsystem.theme.LavenderAccent
import com.techlife.kockit.core.designsystem.theme.LogoBlue
import com.techlife.kockit.core.designsystem.theme.OrangeAccent
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

private object StudyPlanStyle {
    val cardRadius = 16.dp
    val cardBorder = Color(0xFFE8ECF0)
    val dayStepperBg = Color(0xFFECFDF5)
    val dayStepperBtnBg = Color(0xFFD1FAE5)
    val paramStepperBg = Color(0xFFF3F4F6)
    val paramStepperBtnBg = Color(0xFFE9ECEF)
    val emerald = Color(0xFF10B981)
    val emeraldSoft = Color(0xFFECFDF5)
    val autoPlanBorder = Color(0xFF6EE7B7)
    val autoPlanBg = Color(0xFFF0FDF9)
    val backButtonBg = Color(0xFFF3F4F6)
    val dayCardBorder = Color(0xFFE8ECF0)
    val unavailableSelectedBg = Color(0xFFFFF1F0)
    val unavailableSelectedBorder = Color(0xFFFECACA)
    val specialDateBg = Color(0xFFF8FAFC)
    val divider = Color(0xFFF1F5F9)
    val dropdownBg = Color(0xFFF3F4F6)
}

@Composable
private fun studyPlanMetrics() = LocalStudyPlanLayoutMetrics.current

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanHeader(
    onBackClick: () -> Unit,
    onAutoPlanClick: () -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KocKitBackButton(
                onClick = onBackClick,
                size = metrics.backButtonSize,
                iconSize = metrics.backIconSize
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        KocKitBoldText(
            text = "Çalışma Planı",
            color = TextPrimary,
            fontSize = metrics.headerTitleSize,
            lineHeight = metrics.headerTitleLineHeight
        )
        Spacer(modifier = Modifier.height(8.dp))
        KocKitText(
            text = description,
            color = TextSecondary,
            fontSize = metrics.headerSubtitleSize,
            lineHeight = metrics.headerSubtitleLineHeight,
            maxLines = 2
        )
    }
}

@Composable
private fun StudyPlanBorderedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val metrics = studyPlanMetrics()
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(StudyPlanStyle.cardRadius),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, StudyPlanStyle.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(metrics.cardInnerPadding)
        ) {
            content()
        }
    }
}

@Composable
private fun StudyPlanSectionEditAction(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    val label = if (isEditing) "Kaydet" else "Düzenle"
    val textColor = if (isEditing) StudyPlanStyle.emerald else LogoBlue
    val onClick = if (isEditing) onSaveClick else onEditClick

    KocKitSemiText(
        text = label,
        color = textColor,
        fontSize = metrics.badgeTextSize,
        lineHeight = metrics.badgeTextLineHeight,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
fun StudyPlanDaysSection(
    days: List<StudyPlanDay>,
    totalHours: Int,
    onHoursChange: (Int, Int) -> Unit,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    StudyPlanBorderedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null,
                tint = StudyPlanStyle.emerald,
                modifier = Modifier.size(if (metrics.isExpanded) 22.dp else 18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            KocKitSemiText(
                text = "Günlere Göre Dağılım",
                color = TextPrimary,
                fontSize = metrics.sectionTitleSize,
                lineHeight = metrics.sectionTitleLineHeight,
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = StudyPlanStyle.emeraldSoft
            ) {
                KocKitSemiText(
                    text = "Toplam $totalHours Saat",
                    color = StudyPlanStyle.emerald,
                    fontSize = metrics.badgeTextSize,
                    lineHeight = metrics.badgeTextLineHeight,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            StudyPlanSectionEditAction(
                isEditing = isEditing,
                onEditClick = onEditClick,
                onSaveClick = onSaveClick
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        days.chunked(4).forEachIndexed { rowIndex, rowDays ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(if (metrics.isExpanded) 10.dp else 8.dp)
            ) {
                rowDays.forEachIndexed { colIndex, day ->
                    val dayIndex = rowIndex * 4 + colIndex
                    StudyPlanDayCard(
                        day = day,
                        enabled = isEditing,
                        onHoursChange = { onHoursChange(dayIndex, it) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(4 - rowDays.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (rowIndex < days.lastIndex / 4) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StudyPlanDayCard(
    day: StudyPlanDay,
    onHoursChange: (Int) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(if (!enabled) Modifier else Modifier),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, StudyPlanStyle.dayCardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KocKitSemiText(
                    text = day.name,
                    color = TextSecondary,
                    fontSize = metrics.microCaptionSize,
                    lineHeight = metrics.microCaptionLineHeight,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(contentAlignment = Alignment.Center) {
                    if (enabled) {
                        KocKitNumberPickerField(
                            value = day.hours,
                            onValueChange = onHoursChange,
                            range = 0..12,
                            pickerTitle = "${day.name} - Saat",
                            showContainer = false,
                            centerFontSize = metrics.dayHourPickerSize,
                            centerLineHeight = metrics.dayHourPickerLineHeight,
                            modifier = Modifier.width(metrics.dayPickerWidth)
                        )
                    } else {
                        KocKitBoldText(
                            text = day.hours.toString(),
                            color = TextPrimary,
                            fontSize = metrics.dayHourPickerSize,
                            lineHeight = metrics.dayHourPickerLineHeight
                        )
                    }
                }
                KocKitText(
                    text = "Saat",
                    color = TextSecondary,
                    fontSize = metrics.smallCaptionSize,
                    lineHeight = metrics.smallCaptionLineHeight
                )
                if (enabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    StudyPlanDayStepper(
                        value = day.hours,
                        onValueChange = onHoursChange,
                        range = 0..12,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun StudyPlanDayStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StudyPlanDayStepButton(
            text = "−",
            enabled = value > range.first,
            onClick = { onValueChange((value - 1).coerceIn(range)) }
        )
        StudyPlanDayStepButton(
            text = "+",
            enabled = value < range.last,
            onClick = { onValueChange((value + 1).coerceIn(range)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudyPlanDayStepButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val metrics = studyPlanMetrics()
    Surface(
        modifier = Modifier.size(metrics.dayStepButtonSize),
        shape = CircleShape,
        color = if (enabled) StudyPlanStyle.dayStepperBtnBg else StudyPlanStyle.dayStepperBtnBg.copy(alpha = 0.5f),
        shadowElevation = if (enabled) 1.dp else 0.dp,
        onClick = onClick,
        enabled = enabled
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            KocKitBoldText(
                text = text,
                color = if (enabled) StudyPlanStyle.emerald else StudyPlanStyle.emerald.copy(alpha = 0.35f),
                fontSize = if (metrics.isExpanded) 16.sp else 14.sp,
                lineHeight = if (metrics.isExpanded) 16.sp else 14.sp
            )
        }
    }
}

@Composable
fun StudyPlanParameterStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    pickerTitle: String,
    suffix: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    if (!enabled) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            KocKitExtraBoldText(
                text = if (suffix.isBlank()) value.toString() else "$value $suffix",
                color = TextPrimary,
                fontSize = metrics.parameterPickerSize,
                lineHeight = metrics.parameterPickerLineHeight,
                maxLines = 1
            )
        }
        return
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StudyPlanParameterStepButton(
            text = "−",
            enabled = value > range.first,
            onClick = { onValueChange((value - 1).coerceIn(range)) }
        )
        KocKitNumberPickerField(
            value = value,
            onValueChange = onValueChange,
            range = range,
            pickerTitle = pickerTitle,
            suffix = suffix,
            showContainer = false,
            centerFontSize = metrics.parameterPickerSize,
            centerLineHeight = metrics.parameterPickerLineHeight,
            modifier = Modifier.width(if (metrics.isExpanded) 60.dp else 52.dp)
        )
        StudyPlanParameterStepButton(
            text = "+",
            enabled = value < range.last,
            onClick = { onValueChange((value + 1).coerceIn(range)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudyPlanParameterStepButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val metrics = studyPlanMetrics()
    Surface(
        modifier = Modifier.size(metrics.paramStepButtonSize),
        shape = CircleShape,
        color = White,
        border = BorderStroke(1.dp, StudyPlanStyle.cardBorder),
        shadowElevation = if (enabled) 1.dp else 0.dp,
        onClick = onClick,
        enabled = enabled
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            KocKitBoldText(
                text = text,
                color = if (enabled) TextPrimary else TextSecondary.copy(alpha = 0.35f),
                fontSize = if (metrics.isExpanded) 17.sp else 15.sp,
                lineHeight = if (metrics.isExpanded) 17.sp else 15.sp
            )
        }
    }
}

@Composable
fun StudyPlanParametersSection(
    sessionMinutes: Int,
    onSessionChange: (Int) -> Unit,
    paragraphMinutes: Int,
    onParagraphChange: (Int) -> Unit,
    problemMinutes: Int,
    onProblemChange: (Int) -> Unit,
    revisionDay: String?,
    onRevisionDayChange: (String) -> Unit,
    revisionOptions: List<String>,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    StudyPlanBorderedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitSemiText(
                text = "Çalışma Parametreleri",
                color = TextPrimary,
                fontSize = metrics.sectionTitleSize,
                lineHeight = metrics.sectionTitleLineHeight,
                modifier = Modifier.weight(1f)
            )
            StudyPlanSectionEditAction(
                isEditing = isEditing,
                onEditClick = onEditClick,
                onSaveClick = onSaveClick
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        StudyPlanParameterRow(
            icon = Icons.Outlined.Schedule,
            iconTint = LavenderAccent,
            iconBg = LavenderAccent.copy(alpha = 0.12f),
            title = "Oturum Süresi",
            subtitle = "Bir oturumda çalışacağın süre",
            trailing = {
                StudyPlanParameterStepper(
                    value = sessionMinutes,
                    onValueChange = onSessionChange,
                    range = 15..180,
                    pickerTitle = "Oturum Süresi (dk)",
                    suffix = "dk",
                    enabled = isEditing,
                    modifier = Modifier.width(metrics.parameterStepperWidth)
                )
            }
        )
        StudyPlanRowDivider()
        StudyPlanParameterRow(
            icon = Icons.Outlined.MenuBook,
            iconTint = OrangeAccent,
            iconBg = OrangeAccent.copy(alpha = 0.12f),
            title = "Günlük Paragraf Süresi",
            subtitle = "Her gün çözeceğin paragraf süresi",
            trailing = {
                StudyPlanParameterStepper(
                    value = paragraphMinutes,
                    onValueChange = onParagraphChange,
                    range = 0..120,
                    pickerTitle = "Günlük Paragraf Süresi (dk)",
                    suffix = "dk",
                    enabled = isEditing,
                    modifier = Modifier.width(metrics.parameterStepperWidth)
                )
            }
        )
        StudyPlanRowDivider()
        StudyPlanParameterRow(
            icon = Icons.Outlined.Psychology,
            iconTint = StudyPlanStyle.emerald,
            iconBg = StudyPlanStyle.emeraldSoft,
            title = "Günlük Problem Süresi",
            subtitle = "Her gün çözeceğin problem süresi",
            trailing = {
                StudyPlanParameterStepper(
                    value = problemMinutes,
                    onValueChange = onProblemChange,
                    range = 0..120,
                    pickerTitle = "Günlük Problem Süresi (dk)",
                    suffix = "dk",
                    enabled = isEditing,
                    modifier = Modifier.width(metrics.parameterStepperWidth)
                )
            }
        )
        StudyPlanRowDivider()
        StudyPlanParameterRow(
            icon = Icons.Filled.CalendarToday,
            iconTint = LavenderAccent,
            iconBg = LavenderAccent.copy(alpha = 0.12f),
            title = "Genel Tekrar Günü",
            subtitle = "Haftanın bir günü seç",
            trailing = {
                StudyPlanCompactDropdown(
                    selected = revisionDay,
                    options = revisionOptions,
                    onSelected = onRevisionDayChange,
                    enabled = isEditing,
                    modifier = Modifier.width(metrics.dropdownWidth)
                )
            }
        )
    }
}

@Composable
private fun StudyPlanParameterRow(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    val metrics = studyPlanMetrics()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StudyPlanIconChip(icon = icon, tint = iconTint, background = iconBg)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            KocKitSemiText(
                text = title,
                color = TextPrimary,
                fontSize = metrics.rowTitleSize,
                lineHeight = metrics.rowTitleLineHeight
            )
            Spacer(modifier = Modifier.height(2.dp))
            KocKitText(
                text = subtitle,
                color = TextSecondary,
                fontSize = metrics.rowSubtitleSize,
                lineHeight = metrics.rowSubtitleLineHeight,
                maxLines = 2
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun StudyPlanRowDivider() {
    HorizontalDivider(color = StudyPlanStyle.divider, thickness = 1.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanCompactDropdown(
    selected: String?,
    options: List<String>,
    onSelected: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    var expanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (!enabled) {
        KocKitSemiText(
            text = selected.orEmpty(),
            color = TextPrimary,
            fontSize = metrics.parameterPickerSize,
            lineHeight = metrics.parameterPickerLineHeight,
            modifier = modifier,
            maxLines = 1
        )
        return
    }
    Surface(
        onClick = { expanded = true },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = StudyPlanStyle.dropdownBg,
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitSemiText(
                text = selected.orEmpty(),
                color = TextPrimary,
                fontSize = metrics.parameterPickerSize,
                lineHeight = metrics.parameterPickerLineHeight,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
            )
        }
    }
    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            sheetState = sheetState,
            containerColor = White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
            ) {
                KocKitBoldText(
                    text = "Gün Seçin",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onSelected(option)
                                expanded = false
                            }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KocKitSemiText(
                            text = option,
                            color = if (option == selected) StudyPlanStyle.emerald else TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudyPlanUnavailableSection(
    weekDayOptions: List<StudyPlanWeekDayOption>,
    selectedUnavailableDays: Set<String>,
    specialDates: List<StudyPlanSpecialDate>,
    onUnavailableDayToggle: (String) -> Unit,
    onRemoveSpecialDate: (String) -> Unit,
    onAddSpecialDateClick: () -> Unit,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    StudyPlanBorderedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Block,
                contentDescription = null,
                tint = ErrorAccent,
                modifier = Modifier.size(if (metrics.isExpanded) 22.dp else 18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            KocKitSemiText(
                text = "Çalışamayacağım Günler",
                color = TextPrimary,
                fontSize = metrics.sectionTitleSize,
                lineHeight = metrics.sectionTitleLineHeight,
                modifier = Modifier.weight(1f)
            )
            StudyPlanSectionEditAction(
                isEditing = isEditing,
                onEditClick = onEditClick,
                onSaveClick = onSaveClick
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        KocKitText(
            text = "Bu günlerde çalışma programı oluşturulmayacaktır.",
            color = TextSecondary,
            fontSize = metrics.rowSubtitleSize,
            lineHeight = metrics.rowSubtitleLineHeight,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyPlanNumberedSubsection(
            number = "1.",
            icon = Icons.Filled.Sync,
            iconTint = StudyPlanStyle.emerald,
            iconBg = StudyPlanStyle.emeraldSoft,
            title = "Düzenli Uygun Olmadığım Günler",
            subtitle = "Her hafta bu günlerde çalışmam."
        )
        Spacer(modifier = Modifier.height(12.dp))
        StudyPlanUnavailableDaySelector(
            options = weekDayOptions,
            selectedShortNames = selectedUnavailableDays,
            onDayToggle = onUnavailableDayToggle,
            enabled = isEditing
        )
        Spacer(modifier = Modifier.height(16.dp))
        StudyPlanRowDivider()
        Spacer(modifier = Modifier.height(16.dp))
        StudyPlanNumberedSubsection(
            number = "2.",
            icon = Icons.Filled.CalendarToday,
            iconTint = LogoBlue,
            iconBg = LogoBlue.copy(alpha = 0.10f),
            title = "Özel Tarihler",
            subtitle = "Belirli tarihlerde çalışmam."
        )
        Spacer(modifier = Modifier.height(12.dp))
        specialDates.forEach { date ->
            StudyPlanSpecialDateItem(
                item = date,
                onRemove = { onRemoveSpecialDate(date.id) },
                canRemove = isEditing
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (isEditing) {
            StudyPlanAddDateButton(onClick = onAddSpecialDateClick)
        }
    }
}

@Composable
private fun StudyPlanNumberedSubsection(
    number: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String
) {
    val metrics = studyPlanMetrics()
    Row(verticalAlignment = Alignment.Top) {
        StudyPlanIconChip(icon = icon, tint = iconTint, background = iconBg)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            KocKitSemiText(
                text = "$number $title",
                color = TextPrimary,
                fontSize = metrics.rowTitleSize,
                lineHeight = metrics.rowTitleLineHeight
            )
            Spacer(modifier = Modifier.height(2.dp))
            KocKitText(
                text = subtitle,
                color = TextSecondary,
                fontSize = metrics.rowSubtitleSize,
                lineHeight = metrics.rowSubtitleLineHeight,
                maxLines = 2
            )
        }
    }
}

@Composable
fun StudyPlanUnavailableDaySelector(
    options: List<StudyPlanWeekDayOption>,
    selectedShortNames: Set<String>,
    onDayToggle: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (metrics.isExpanded) 8.dp else 6.dp)
    ) {
        options.forEach { option ->
            val selected = option.shortName in selectedShortNames
            val bg = if (selected) StudyPlanStyle.unavailableSelectedBg else White
            val borderColor = if (selected) StudyPlanStyle.unavailableSelectedBorder else StudyPlanStyle.dayCardBorder
            val textColor = if (selected) CoralAccent else TextSecondary
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(metrics.unavailableDayHeight)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .then(
                        if (enabled) {
                            Modifier.clickable { onDayToggle(option.shortName) }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(CoralAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(7.dp)
                        )
                    }
                }
                KocKitSemiText(
                    text = option.shortName,
                    color = textColor,
                    fontSize = metrics.smallCaptionSize,
                    lineHeight = metrics.smallCaptionLineHeight
                )
            }
        }
    }
}

@Composable
fun StudyPlanSpecialDateItem(
    item: StudyPlanSpecialDate,
    onRemove: () -> Unit,
    canRemove: Boolean = true,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = StudyPlanStyle.specialDateBg,
        border = BorderStroke(1.dp, StudyPlanStyle.cardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                KocKitSemiText(
                    text = item.title,
                    color = TextPrimary,
                    fontSize = metrics.parameterPickerSize,
                    lineHeight = metrics.parameterPickerLineHeight,
                    maxLines = 2
                )
                if (item.subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitText(
                        text = item.subtitle,
                        color = TextSecondary,
                        fontSize = metrics.smallCaptionSize,
                        lineHeight = metrics.smallCaptionLineHeight,
                        maxLines = 1
                    )
                }
            }
            if (canRemove) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(if (metrics.isExpanded) 32.dp else 28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Kaldır",
                        tint = TextSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(if (metrics.isExpanded) 16.dp else 14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StudyPlanAddDateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = LogoBlue,
            modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        KocKitSemiText(
            text = "Tarih veya Tarih Aralığı Ekle",
            color = LogoBlue,
            fontSize = metrics.parameterPickerSize,
            lineHeight = metrics.parameterPickerLineHeight
        )
    }
}

@Composable
fun StudyPlanSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.saveButtonHeight),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = StudyPlanStyle.emerald,
            contentColor = White
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = null,
            modifier = Modifier.size(if (metrics.isExpanded) 20.dp else 18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        KocKitBoldText(
            text = "Kaydet",
            color = White,
            fontSize = metrics.saveButtonTextSize,
            lineHeight = metrics.saveButtonTextLineHeight
        )
    }
}

@Composable
fun StudyPlanInfoNote(
    text: String,
    modifier: Modifier = Modifier
) {
    val metrics = studyPlanMetrics()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(StudyPlanStyle.emeraldSoft)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Filled.Lightbulb,
            contentDescription = null,
            tint = StudyPlanStyle.emerald,
            modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        KocKitText(
            text = text,
            color = TextSecondary,
            fontSize = metrics.rowSubtitleSize,
            lineHeight = metrics.rowSubtitleLineHeight,
            maxLines = 3
        )
    }
}

@Composable
private fun StudyPlanIconChip(
    icon: ImageVector,
    tint: Color,
    background: Color
) {
    val metrics = studyPlanMetrics()
    Box(
        modifier = Modifier
            .size(metrics.iconChipSize)
            .clip(RoundedCornerShape(8.dp))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(metrics.iconChipIconSize)
        )
    }
}

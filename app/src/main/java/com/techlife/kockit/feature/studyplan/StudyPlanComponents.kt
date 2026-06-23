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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitNumberPickerField
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanHeader(
    onBackClick: () -> Unit,
    onAutoPlanClick: () -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    modifier = Modifier.size(24.dp),
                    tint = TextPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        KocKitBoldText(
            text = "Çalışma Planı",
            color = TextPrimary,
            fontSize = 24.sp,
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        KocKitText(
            text = description,
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun StudyPlanBorderedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
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
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun StudyPlanDaysSection(
    days: List<StudyPlanDay>,
    totalHours: Int,
    onHoursChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    StudyPlanBorderedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null,
                tint = StudyPlanStyle.emerald,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            KocKitSemiText(
                text = "Günlere Göre Dağılım",
                color = TextPrimary,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = StudyPlanStyle.emeraldSoft
            ) {
                KocKitSemiText(
                    text = "Toplam $totalHours Saat",
                    color = StudyPlanStyle.emerald,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        days.chunked(4).forEachIndexed { rowIndex, rowDays ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowDays.forEachIndexed { colIndex, day ->
                    val dayIndex = rowIndex * 4 + colIndex
                    StudyPlanDayCard(
                        day = day,
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
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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
                    fontSize = 9.sp,
                    lineHeight = 13.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(contentAlignment = Alignment.Center) {
                    KocKitNumberPickerField(
                        value = day.hours,
                        onValueChange = onHoursChange,
                        range = 0..12,
                        pickerTitle = "${day.name} - Saat",
                        showContainer = false,
                        centerFontSize = 22.sp,
                        centerLineHeight = 24.sp,
                        modifier = Modifier.width(40.dp)
                    )
                }
                KocKitText(
                    text = "Saat",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                )
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
    Surface(
        modifier = Modifier.size(22.dp),
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
                fontSize = 14.sp,
                lineHeight = 14.sp
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
    modifier: Modifier = Modifier
) {
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
            centerFontSize = 12.sp,
            centerLineHeight = 14.sp,
            modifier = Modifier.width(52.dp)
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
    Surface(
        modifier = Modifier.size(28.dp),
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
                fontSize = 15.sp,
                lineHeight = 15.sp
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
    modifier: Modifier = Modifier
) {
    StudyPlanBorderedCard(modifier = modifier) {
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
                    modifier = Modifier.width(118.dp)
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
                    modifier = Modifier.width(118.dp)
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
                    modifier = Modifier.width(118.dp)
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
                    modifier = Modifier.width(88.dp)
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
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            KocKitText(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 14.sp,
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
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
                fontSize = 12.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
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
    modifier: Modifier = Modifier
) {
    StudyPlanBorderedCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Outlined.Block,
                contentDescription = null,
                tint = ErrorAccent,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                KocKitSemiText(
                    text = "Çalışamayacağım Günler",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                KocKitText(
                    text = "Bu günlerde çalışma programı oluşturulmayacaktır.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2
                )
            }
        }
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
            onDayToggle = onUnavailableDayToggle
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
                onRemove = { onRemoveSpecialDate(date.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        StudyPlanAddDateButton(onClick = onAddSpecialDateClick)
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
    Row(verticalAlignment = Alignment.Top) {
        StudyPlanIconChip(icon = icon, tint = iconTint, background = iconBg)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            KocKitSemiText(
                text = "$number $title",
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            KocKitText(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 14.sp,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        options.forEach { option ->
            val selected = option.shortName in selectedShortNames
            val bg = if (selected) StudyPlanStyle.unavailableSelectedBg else White
            val borderColor = if (selected) StudyPlanStyle.unavailableSelectedBorder else StudyPlanStyle.dayCardBorder
            val textColor = if (selected) CoralAccent else TextSecondary
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .clickable { onDayToggle(option.shortName) },
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
                    fontSize = 11.sp,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
fun StudyPlanSpecialDateItem(
    item: StudyPlanSpecialDate,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
                if (item.subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    KocKitText(
                        text = item.subtitle,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        maxLines = 1
                    )
                }
            }
            IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Kaldır",
                    tint = TextSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun StudyPlanAddDateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        KocKitSemiText(
            text = "Tarih veya Tarih Aralığı Ekle",
            color = LogoBlue,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun StudyPlanSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = StudyPlanStyle.emerald,
            contentColor = White
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        KocKitBoldText(
            text = "Kaydet",
            color = White,
            fontSize = 16.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun StudyPlanInfoNote(
    text: String,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        KocKitText(
            text = text,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp,
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
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

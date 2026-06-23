package com.techlife.kockit.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.White
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlin.math.abs

private val PickerItemHeight = 44.dp
private val PickerVisibleRows = 5

@Composable
private fun KocKitNumberPickerWheel(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    val colors = KocKitTheme.extraColors
    val items = remember(range) { range.toList() }
    val wheelHeight = PickerItemHeight * PickerVisibleRows
    val initialIndex = (value - range.first).coerceIn(0, items.lastIndex)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState, items) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { !it }
            .collect {
                val layoutInfo = listState.layoutInfo
                if (layoutInfo.visibleItemsInfo.isEmpty()) return@collect
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    abs((item.offset + item.size / 2) - viewportCenter)
                } ?: return@collect
                onValueChange(items[centerItem.index])
            }
    }

    LaunchedEffect(initialIndex) {
        listState.scrollToItem(initialIndex)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(wheelHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(PickerItemHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.pastelGreen.copy(alpha = 0.18f))
        )
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = wheelHeight / 2 - PickerItemHeight / 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { _, item ->
                val isSelected = item == value
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PickerItemHeight)
                        .clickable { onValueChange(item) },
                    contentAlignment = Alignment.Center
                ) {
                    KocKitExtraBoldText(
                        text = item.toString(),
                        color = if (isSelected) colors.textPrimary else colors.textSecondary.copy(alpha = 0.45f),
                        fontSize = if (isSelected) 24.sp else 17.sp,
                        lineHeight = if (isSelected) 28.sp else 20.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KocKitNumberPickerField(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    pickerTitle: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    compact: Boolean = false,
    showContainer: Boolean = true,
    centerFontSize: androidx.compose.ui.unit.TextUnit? = null,
    centerLineHeight: androidx.compose.ui.unit.TextUnit? = null
) {
    val colors = KocKitTheme.extraColors
    var expanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val displayText = if (suffix.isNullOrBlank()) {
        value.toString()
    } else {
        "$value $suffix"
    }

    val triggerModifier = modifier
        .then(
            when {
                !showContainer -> Modifier.clickable { expanded = true }
                compact -> Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .clickable { expanded = true }
                else -> Modifier
                    .width(72.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .clickable { expanded = true }
            }
        )

    Box(
        modifier = triggerModifier,
        contentAlignment = Alignment.Center
    ) {
        KocKitExtraBoldText(
            text = displayText,
            color = TextPrimary,
            fontSize = centerFontSize ?: when {
                !showContainer && suffix != null -> 12.sp
                !showContainer -> 12.sp
                compact -> 14.sp
                else -> 13.sp
            },
            lineHeight = centerLineHeight ?: when {
                !showContainer && suffix != null -> 14.sp
                !showContainer -> 14.sp
                compact -> 16.sp
                else -> 15.sp
            },
            maxLines = 1
        )
    }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = false },
            sheetState = sheetState,
            containerColor = colors.cardBackground,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            var tempValue by remember(value) { mutableIntStateOf(value.coerceIn(range)) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KocKitBoldText(
                    text = pickerTitle,
                    color = colors.textPrimary,
                    fontSize = KocKitTextDefaults.fontSizeTitle,
                    lineHeight = KocKitTextDefaults.lineHeightTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                key(value) {
                    KocKitNumberPickerWheel(
                        value = tempValue,
                        onValueChange = { tempValue = it },
                        range = range
                    )
                }
                KocKitPrimaryButton(
                    text = "Tamam",
                    onClick = {
                        onValueChange(tempValue.coerceIn(range))
                        expanded = false
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KocKitNumberPickerFieldPreview() {
    KocKitTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            var value by remember { mutableIntStateOf(24) }
            KocKitNumberPickerField(
                value = value,
                onValueChange = { value = it },
                range = 0..24,
                pickerTitle = "Saat seçin",
                compact = true
            )
        }
    }
}

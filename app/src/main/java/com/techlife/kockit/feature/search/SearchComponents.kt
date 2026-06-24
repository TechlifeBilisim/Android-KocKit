package com.techlife.kockit.feature.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitExtraBoldText
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.layout.LocalSearchLayoutMetrics
import com.techlife.kockit.core.designsystem.theme.BorderLight
import com.techlife.kockit.core.designsystem.theme.KocKitFontFamily
import com.techlife.kockit.core.designsystem.theme.PastelGreen
import com.techlife.kockit.core.designsystem.theme.TextPrimary
import com.techlife.kockit.core.designsystem.theme.TextSecondary
import com.techlife.kockit.core.designsystem.theme.White

@Composable
private fun searchMetrics() = LocalSearchLayoutMetrics.current

@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = searchMetrics()
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.size(metrics.backButtonSize),
            shape = CircleShape,
            color = White,
            shadowElevation = 2.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = TextPrimary,
                    modifier = Modifier.size(metrics.backIconSize)
                )
            }
        }
        SearchInputField(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier.weight(1f)
        )
        Surface(
            modifier = Modifier.size(metrics.backButtonSize),
            shape = CircleShape,
            color = White,
            shadowElevation = 2.dp
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "Sesli arama",
                    tint = TextPrimary,
                    modifier = Modifier.size(metrics.actionIconSize)
                )
            }
        }
    }
}

@Composable
private fun SearchInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = searchMetrics()
    Surface(
        modifier = modifier.height(metrics.searchFieldHeight),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(metrics.searchIconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = KocKitFontFamily,
                    fontSize = metrics.searchTextSize,
                    lineHeight = metrics.searchTextLineHeight,
                    color = TextPrimary
                ),
                cursorBrush = SolidColor(PastelGreen),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (query.isEmpty()) {
                            KocKitText(
                                text = "Ders, konu, test veya deneme ara...",
                                color = TextSecondary,
                                fontSize = metrics.placeholderSize,
                                lineHeight = metrics.placeholderLineHeight,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun SearchRecentSection(
    items: List<String>,
    onClearAll: () -> Unit,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = searchMetrics()
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitBoldText(
                text = "Son Aramalar",
                color = TextPrimary,
                fontSize = metrics.sectionTitleSize,
                lineHeight = metrics.sectionTitleLineHeight
            )
            if (items.isNotEmpty()) {
                Row(
                    modifier = Modifier.clickable(onClick = onClearAll),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = PastelGreen,
                        modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
                    )
                    KocKitSemiText(
                        text = "Temizle",
                        color = PastelGreen,
                        fontSize = metrics.linkTextSize,
                        lineHeight = metrics.linkTextLineHeight
                    )
                }
            }
        }
        if (items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(metrics.headerContentGap))
            SearchWrapRow(
                horizontalSpacing = if (metrics.isExpanded) 12.dp else 10.dp,
                verticalSpacing = if (metrics.isExpanded) 12.dp else 10.dp
            ) {
                items.forEach { item ->
                    SearchRecentChip(
                        text = item,
                        onRemove = { onRemoveItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchWrapRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier.fillMaxWidth(),
        content = content
    ) { measurables, constraints ->
        val horizontalGap = horizontalSpacing.roundToPx()
        val verticalGap = verticalSpacing.roundToPx()
        val maxWidth = constraints.maxWidth

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        var x = 0
        var y = 0
        var rowHeight = 0
        val positions = ArrayList<Pair<Int, Int>>(placeables.size)

        placeables.forEach { placeable ->
            if (x > 0 && x + placeable.width > maxWidth) {
                x = 0
                y += rowHeight + verticalGap
                rowHeight = 0
            }
            positions.add(x to y)
            x += placeable.width + horizontalGap
            rowHeight = maxOf(rowHeight, placeable.height)
        }

        val height = if (placeables.isEmpty()) 0 else y + rowHeight
        layout(maxWidth, height) {
            placeables.forEachIndexed { index, placeable ->
                val (positionX, positionY) = positions[index]
                placeable.placeRelative(positionX, positionY)
            }
        }
    }
}

@Composable
private fun SearchRecentChip(
    text: String,
    onRemove: () -> Unit
) {
    val metrics = searchMetrics()
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = metrics.chipPaddingH,
                vertical = metrics.chipPaddingV
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.7f),
                modifier = Modifier.size(metrics.chipIconSize)
            )
            KocKitText(
                text = text,
                color = TextPrimary,
                fontSize = metrics.chipTextSize,
                lineHeight = metrics.chipTextLineHeight
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Kaldır",
                tint = TextSecondary,
                modifier = Modifier
                    .size(metrics.chipIconSize)
                    .clickable(onClick = onRemove)
            )
        }
    }
}

@Composable
fun SearchPopularTopicsSection(
    topics: List<SearchPopularTopic>,
    modifier: Modifier = Modifier
) {
    val metrics = searchMetrics()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(metrics.headerContentGap)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KocKitBoldText(
                text = "Popüler Konular",
                color = TextPrimary,
                fontSize = metrics.sectionTitleSize,
                lineHeight = metrics.sectionTitleLineHeight
            )
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                KocKitSemiText(
                    text = "Tümünü Gör",
                    color = PastelGreen,
                    fontSize = metrics.linkTextSize,
                    lineHeight = metrics.linkTextLineHeight
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = PastelGreen,
                    modifier = Modifier.size(if (metrics.isExpanded) 18.dp else 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        topics.chunked(2).forEach { rowTopics ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(metrics.gridGap)
            ) {
                rowTopics.forEach { topic ->
                    SearchPopularTopicCard(
                        topic = topic,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                if (rowTopics.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SearchPopularTopicCard(
    topic: SearchPopularTopic,
    modifier: Modifier = Modifier
) {
    val metrics = searchMetrics()
    Card(
        modifier = modifier.clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = topic.cardBg),
        border = BorderStroke(1.dp, topic.iconTint.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(metrics.cardInnerPadding),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(metrics.topicIconBoxSize)
                    .clip(RoundedCornerShape(8.dp))
                    .background(topic.iconBg),
                contentAlignment = Alignment.Center
            ) {
                if (topic.title == "Matematik") {
                    KocKitExtraBoldText(
                        text = "√",
                        color = topic.iconTint,
                        fontSize = metrics.topicIconTextSize,
                        lineHeight = metrics.topicIconTextSize * 1.15f
                    )
                } else {
                    Icon(
                        imageVector = topic.icon,
                        contentDescription = null,
                        tint = topic.iconTint,
                        modifier = Modifier.size(metrics.topicIconGraphicSize)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                KocKitBoldText(
                    text = topic.title,
                    color = TextPrimary,
                    fontSize = metrics.topicTitleSize,
                    lineHeight = metrics.topicTitleLineHeight,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                KocKitText(
                    text = topic.subtitle,
                    color = TextSecondary,
                    fontSize = metrics.topicSubtitleSize,
                    lineHeight = metrics.topicSubtitleLineHeight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(metrics.topicArrowSize)
            )
        }
    }
}

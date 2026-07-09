package com.techlife.kockit.feature.map.components

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.techlife.kockit.feature.map.model.TurkeyMapData
import com.techlife.kockit.feature.map.model.TurkeyProvince
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

private data class ProvinceLabelLayout(
    val textSize: Float,
    val centerX: Float,
    val centerY: Float
)

private data class ProvinceDrawItem(
    val province: TurkeyProvince,
    val screenPath: AndroidPath,
    val region: Region,
    val hitBounds: RectF,
    val labelLayout: ProvinceLabelLayout?
)

private val LABEL_Y_OFFSETS_BY_ID = mapOf(
    "erzincan" to -16f,
    "antalya" to -26f,
    "agri" to -16f,
    "izmir" to 32f
)

@Composable
fun TurkeyMapCanvas(
    mapData: TurkeyMapData,
    modifier: Modifier = Modifier,
    selectedPlateCode: String? = null,
    selectedProvinceId: String? = null,
    defaultProvinceColor: Color = Color(0xFFE5E7EB),
    selectedProvinceColor: Color = Color(0xFF2563EB),
    strokeColor: Color = Color.White,
    labelColor: Color = Color(0xFF172033),
    onProvinceClick: (TurkeyProvince) -> Unit
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var drawItems by remember { mutableStateOf(emptyList<ProvinceDrawItem>()) }

    LaunchedEffect(mapData, canvasSize) {
        val width = canvasSize.width
        val height = canvasSize.height
        if (width <= 0 || height <= 0) {
            drawItems = emptyList()
            return@LaunchedEffect
        }
        drawItems = withContext(Dispatchers.Default) {
            buildDrawItems(
                mapData = mapData,
                canvasWidth = width.toFloat(),
                canvasHeight = height.toFloat()
            )
        }
    }

    val fillPaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    }
    val strokePaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
    }
    val textPaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    val defaultFillArgb = remember(defaultProvinceColor) { defaultProvinceColor.toArgb() }
    val selectedFillArgb = remember(selectedProvinceColor) { selectedProvinceColor.toArgb() }
    val strokeArgb = remember(strokeColor) { strokeColor.toArgb() }
    val labelArgb = remember(labelColor) { labelColor.toArgb() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = it }
            .pointerInput(drawItems) {
                detectTapGestures { offset ->
                    val x = offset.x.toInt()
                    val y = offset.y.toInt()
                    drawItems
                        .asReversed()
                        .firstOrNull { item ->
                            item.hitBounds.contains(offset.x, offset.y) &&
                                item.region.contains(x, y)
                        }
                        ?.let { onProvinceClick(it.province) }
                }
            }
    ) {
        if (drawItems.isEmpty()) return@Canvas

        strokePaint.color = strokeArgb
        textPaint.color = labelArgb

        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            drawItems.forEach { item ->
                val isSelected = item.province.plateCode == selectedPlateCode &&
                    (selectedProvinceId == null || item.province.id == selectedProvinceId)

                fillPaint.color = if (isSelected) selectedFillArgb else defaultFillArgb
                nativeCanvas.drawPath(item.screenPath, fillPaint)
                nativeCanvas.drawPath(item.screenPath, strokePaint)

                item.labelLayout?.let { layout ->
                    textPaint.textSize = layout.textSize
                    nativeCanvas.drawText(
                        item.province.name,
                        layout.centerX,
                        layout.centerY,
                        textPaint
                    )
                }
            }
        }
    }
}

private fun computeLabelLayout(
    provinceId: String,
    label: String,
    bounds: RectF,
    measurePaint: Paint
): ProvinceLabelLayout? {
    if (bounds.width() < 14f || bounds.height() < 10f) return null

    val maxTextSize = min(bounds.width(), bounds.height()) * 0.28f
    var textSize = maxTextSize.coerceIn(6f, 13f)
    measurePaint.textSize = textSize
    while (measurePaint.measureText(label) > bounds.width() * 0.92f && textSize > 5f) {
        textSize -= 0.5f
        measurePaint.textSize = textSize
    }
    if (measurePaint.measureText(label) > bounds.width()) return null

    val labelYOffset = LABEL_Y_OFFSETS_BY_ID[provinceId] ?: 0f
    return ProvinceLabelLayout(
        textSize = textSize,
        centerX = bounds.centerX(),
        centerY = bounds.centerY() -
            (measurePaint.descent() + measurePaint.ascent()) / 2f +
            labelYOffset
    )
}

private fun buildDrawItems(
    mapData: TurkeyMapData,
    canvasWidth: Float,
    canvasHeight: Float
): List<ProvinceDrawItem> {
    val viewBox = mapData.viewBox
    val viewBoxWidth = viewBox.width().coerceAtLeast(1f)
    val viewBoxHeight = viewBox.height().coerceAtLeast(1f)

    val scale = minOf(canvasWidth / viewBoxWidth, canvasHeight / viewBoxHeight)
    val dx = (canvasWidth - viewBoxWidth * scale) / 2f - viewBox.left * scale
    val dy = (canvasHeight - viewBoxHeight * scale) / 2f - viewBox.top * scale

    val matrix = Matrix().apply {
        setScale(scale, scale)
        postTranslate(dx, dy)
    }
    val measurePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    return mapData.provinces.mapNotNull { province ->
        val screenPath = AndroidPath()
        province.path.transform(matrix, screenPath)

        val bounds = RectF()
        screenPath.computeBounds(bounds, true)
        if (bounds.isEmpty) return@mapNotNull null

        val clip = Region(
            floor(bounds.left).toInt(),
            floor(bounds.top).toInt(),
            ceil(bounds.right).toInt(),
            ceil(bounds.bottom).toInt()
        )
        val region = Region().apply { setPath(screenPath, clip) }

        ProvinceDrawItem(
            province = province,
            screenPath = screenPath,
            region = region,
            hitBounds = RectF(bounds),
            labelLayout = computeLabelLayout(
                provinceId = province.id,
                label = province.name,
                bounds = bounds,
                measurePaint = measurePaint
            )
        )
    }
}

private fun Color.toArgb(): Int = android.graphics.Color.argb(
    (alpha * 255f).toInt(),
    (red * 255f).toInt(),
    (green * 255f).toInt(),
    (blue * 255f).toInt()
)

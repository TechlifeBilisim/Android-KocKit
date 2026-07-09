package com.techlife.kockit.feature.map.model

import android.graphics.Path
import android.graphics.RectF

data class TurkeyProvince(
    val id: String,
    val plateCode: String,
    val areaCode: String,
    val name: String,
    val path: Path,
    val bounds: RectF
)

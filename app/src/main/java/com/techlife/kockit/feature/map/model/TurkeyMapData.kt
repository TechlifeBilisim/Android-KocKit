package com.techlife.kockit.feature.map.model

import android.graphics.RectF

data class TurkeyMapData(
    val viewBox: RectF,
    val provinces: List<TurkeyProvince>
) {
    companion object {
        val EMPTY = TurkeyMapData(
            viewBox = RectF(0f, 0f, 1007.478f, 527.323f),
            provinces = emptyList()
        )
    }
}

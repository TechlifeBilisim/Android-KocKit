package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.data.remote.dto.yo.YoBilimDto
import com.techlife.kockit.data.remote.dto.yo.YoFakulteDto
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoFakulte

fun YoBilimDto.toDomain(): YoBilim = YoBilim(
    id = yoBilimId,
    name = ad
)

fun YoFakulteDto.toDomain(): YoFakulte = YoFakulte(
    id = yoFakulteId,
    name = ad
)

fun List<YoBilimDto>.toYoBilimDomain(): List<YoBilim> = map { it.toDomain() }

fun List<YoFakulteDto>.toYoFakulteDomain(): List<YoFakulte> = map { it.toDomain() }

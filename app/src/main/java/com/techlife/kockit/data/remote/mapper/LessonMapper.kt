package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.data.remote.dto.lesson.LessonDto
import com.techlife.kockit.domain.lesson.model.Lesson

fun LessonDto.toDomain(): Lesson = Lesson(
    id = dersId,
    name = ad,
    code = kod
)

fun List<LessonDto>.toDomain(): List<Lesson> = map { it.toDomain() }

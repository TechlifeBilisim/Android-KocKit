package com.techlife.kockit.domain.lesson.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.lesson.model.Lesson

interface LessonRepository {
    suspend fun getLessons(): ApiResult<List<Lesson>>
}

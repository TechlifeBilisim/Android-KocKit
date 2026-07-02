package com.techlife.kockit.data.lesson.repository

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.datasource.LessonRemoteDataSource
import com.techlife.kockit.domain.lesson.model.Lesson
import com.techlife.kockit.domain.lesson.repository.LessonRepository
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(
    private val lessonRemoteDataSource: LessonRemoteDataSource
) : LessonRepository {

    override suspend fun getLessons(): ApiResult<List<Lesson>> =
        lessonRemoteDataSource.getLessons()
}

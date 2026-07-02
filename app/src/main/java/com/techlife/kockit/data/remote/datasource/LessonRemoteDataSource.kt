package com.techlife.kockit.data.remote.datasource

import com.techlife.kockit.core.network.factory.ApiServiceFactory
import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.data.remote.RemoteDataSource
import com.techlife.kockit.data.remote.api.LessonApiService
import com.techlife.kockit.data.remote.mapper.toDomain
import com.techlife.kockit.data.remote.util.requireData
import com.techlife.kockit.domain.lesson.model.Lesson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRemoteDataSource @Inject constructor(
    apiServiceFactory: ApiServiceFactory
) : RemoteDataSource() {

    private val lessonApi: LessonApiService = apiServiceFactory.create()

    suspend fun getLessons(): ApiResult<List<Lesson>> = execute {
        lessonApi.getLessons().requireData().toDomain()
    }
}

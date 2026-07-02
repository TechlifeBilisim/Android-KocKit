package com.techlife.kockit.data.remote.api

import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.annotation.ApiServices
import com.techlife.kockit.data.remote.dto.common.ApiEnvelopeDto
import com.techlife.kockit.data.remote.dto.lesson.LessonDto
import retrofit2.http.GET

interface LessonApiService {

    @GET("api/lesson")
    @ApiLog(ApiServices.LESSON_LIST)
    suspend fun getLessons(): ApiEnvelopeDto<List<LessonDto>>
}

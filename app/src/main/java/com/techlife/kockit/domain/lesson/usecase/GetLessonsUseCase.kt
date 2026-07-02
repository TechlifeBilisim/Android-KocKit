package com.techlife.kockit.domain.lesson.usecase

import com.techlife.kockit.core.network.model.ApiResult
import com.techlife.kockit.domain.lesson.model.Lesson
import com.techlife.kockit.domain.lesson.repository.LessonRepository
import javax.inject.Inject

class GetLessonsUseCase @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(): ApiResult<List<Lesson>> = lessonRepository.getLessons()
}

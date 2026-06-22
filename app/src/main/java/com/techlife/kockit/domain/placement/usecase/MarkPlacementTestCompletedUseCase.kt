package com.techlife.kockit.domain.placement.usecase

import com.techlife.kockit.domain.placement.repository.PlacementRepository
import javax.inject.Inject

class MarkPlacementTestCompletedUseCase @Inject constructor(
    private val placementRepository: PlacementRepository
) {
    suspend operator fun invoke(sectionKey: String) {
        placementRepository.markSectionCompleted(sectionKey)
    }
}

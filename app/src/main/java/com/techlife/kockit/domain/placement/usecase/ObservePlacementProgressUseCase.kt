package com.techlife.kockit.domain.placement.usecase

import com.techlife.kockit.domain.placement.model.PlacementProgress
import com.techlife.kockit.domain.placement.repository.PlacementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePlacementProgressUseCase @Inject constructor(
    private val placementRepository: PlacementRepository
) {
    operator fun invoke(): Flow<PlacementProgress> = placementRepository.observePlacementProgress()
}

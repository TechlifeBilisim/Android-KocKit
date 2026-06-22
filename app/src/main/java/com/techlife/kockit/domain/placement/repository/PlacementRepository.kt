package com.techlife.kockit.domain.placement.repository

import com.techlife.kockit.domain.placement.model.PlacementProgress
import kotlinx.coroutines.flow.Flow

interface PlacementRepository {
    fun observePlacementProgress(): Flow<PlacementProgress>
    suspend fun markSectionCompleted(sectionKey: String)
}

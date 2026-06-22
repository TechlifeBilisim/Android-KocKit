package com.techlife.kockit.data.placement.repository

import com.techlife.kockit.core.datastore.UserPreferences
import com.techlife.kockit.domain.placement.model.PlacementProgress
import com.techlife.kockit.domain.placement.repository.PlacementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacementRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : PlacementRepository {

    override fun observePlacementProgress(): Flow<PlacementProgress> =
        userPreferences.placementProgressFlow

    override suspend fun markSectionCompleted(sectionKey: String) {
        userPreferences.setPlacementSectionCompleted(sectionKey, completed = true)
    }
}

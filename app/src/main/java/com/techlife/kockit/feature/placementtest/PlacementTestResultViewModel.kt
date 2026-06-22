package com.techlife.kockit.feature.placementtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techlife.kockit.domain.placement.usecase.MarkPlacementTestCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PlacementTestResultViewModel @Inject constructor(
    private val markPlacementTestCompletedUseCase: MarkPlacementTestCompletedUseCase
) : ViewModel() {

    fun onResultShown(section: PlacementTestSection) {
        viewModelScope.launch {
            markPlacementTestCompletedUseCase(section.routeKey)
        }
    }
}

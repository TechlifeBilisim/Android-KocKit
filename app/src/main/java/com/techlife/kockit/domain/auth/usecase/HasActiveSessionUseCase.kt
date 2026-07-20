package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.model.StartupDestination
import javax.inject.Inject

class HasActiveSessionUseCase @Inject constructor(
    private val getStartupDestinationUseCase: GetStartupDestinationUseCase
) {
    suspend operator fun invoke(): Boolean =
        getStartupDestinationUseCase() != StartupDestination.Login
}

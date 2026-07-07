package com.techlife.kockit.domain.auth.usecase

import com.techlife.kockit.domain.auth.repository.AuthRepository
import javax.inject.Inject

/**
 * "Beni hatırla" için saklanan telefon numarasını döndürür (yoksa null).
 */
class GetRememberedPhoneUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? = authRepository.getRememberedPhone()
}

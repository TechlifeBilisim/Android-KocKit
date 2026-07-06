package com.techlife.kockit.data.remote.dto.common

/**
 * API yanıtında `data` alanı boş veya null olduğunda kullanılır.
 * Moshi [Unit] tipi için converter oluşturamaz.
 */
data class EmptyDataDto(
    val ignored: String? = null
)

package com.techlife.kockit.core.network.util

import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonFormatter @Inject constructor() {

    fun format(raw: String?): String {
        if (raw.isNullOrBlank()) return "—"
        val trimmed = raw.trim()
        return runCatching {
            when {
                trimmed.startsWith("{") -> JSONObject(trimmed).toString(INDENT_SPACES)
                trimmed.startsWith("[") -> JSONArray(trimmed).toString(INDENT_SPACES)
                else -> trimmed
            }
        }.getOrElse { trimmed }
    }

    private companion object {
        const val INDENT_SPACES = 2
    }
}

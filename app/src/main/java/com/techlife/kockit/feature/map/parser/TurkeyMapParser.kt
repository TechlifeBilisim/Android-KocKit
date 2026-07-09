package com.techlife.kockit.feature.map.parser

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.RawRes
import androidx.core.graphics.PathParser
import com.techlife.kockit.feature.map.model.TurkeyMapData
import com.techlife.kockit.feature.map.model.TurkeyProvince
import java.io.InputStreamReader

object TurkeyMapParser {

    private val DEFAULT_VIEW_BOX = RectF(0f, 0f, 1007.478f, 527.323f)

    private val VIEW_BOX_REGEX = Regex(
        """viewBox\s*=\s*["']([^"']+)["']""",
        RegexOption.IGNORE_CASE
    )

    private val PROVINCE_GROUP_REGEX = Regex(
        """<g\s+id="([^"]+)"[^>]*data-plakakodu="([^"]*)"[^>]*data-alankodu="([^"]*)"[^>]*data-iladi="([^"]*)"[^>]*>(.*?)</g>""",
        setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )

    private val PROVINCE_GROUP_ALT_REGEX = Regex(
        """<g\s+id="([^"]+)"[^>]*data-iladi="([^"]*)"[^>]*data-plakakodu="([^"]*)"[^>]*data-alankodu="([^"]*)"[^>]*>(.*?)</g>""",
        setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )

    private val PATH_DATA_REGEX = Regex(
        """<path[^>]*\sd="([^"]+)"""",
        RegexOption.IGNORE_CASE
    )

    fun load(context: Context, @RawRes resId: Int): TurkeyMapData {
        val html = context.resources.openRawResource(resId).use { input ->
            InputStreamReader(input, Charsets.UTF_8).readText()
        }
        return parse(html)
    }

    fun parse(html: String): TurkeyMapData {
        val viewBox = parseViewBox(html) ?: DEFAULT_VIEW_BOX
        val provinces = buildList {
            PROVINCE_GROUP_REGEX.findAll(html).forEach { match ->
                addProvinceOrNull(
                    id = match.groupValues[1],
                    plateCode = match.groupValues[2],
                    areaCode = match.groupValues[3],
                    name = match.groupValues[4],
                    groupContent = match.groupValues[5]
                )?.let(::add)
            }
            PROVINCE_GROUP_ALT_REGEX.findAll(html).forEach { match ->
                val id = match.groupValues[1]
                if (any { it.id == id }) return@forEach
                addProvinceOrNull(
                    id = id,
                    plateCode = match.groupValues[3],
                    areaCode = match.groupValues[4],
                    name = match.groupValues[2],
                    groupContent = match.groupValues[5]
                )?.let(::add)
            }
        }.distinctBy { it.id }
            .let(::mergeIstanbulProvinces)
            .sortedBy { it.plateCode.padStart(2, '0') + it.id }

        return TurkeyMapData(viewBox = viewBox, provinces = provinces)
    }

    private fun mergeIstanbulProvinces(provinces: List<TurkeyProvince>): List<TurkeyProvince> {
        val asya = provinces.find { it.id == "istanbul-asya" } ?: return provinces
        val avrupa = provinces.find { it.id == "istanbul-avrupa" } ?: return provinces

        val combinedPath = Path().apply {
            addPath(asya.path)
            addPath(avrupa.path)
        }
        val bounds = RectF()
        combinedPath.computeBounds(bounds, true)
        if (bounds.isEmpty) return provinces

        val istanbul = TurkeyProvince(
            id = "istanbul",
            plateCode = "34",
            areaCode = avrupa.areaCode.ifBlank { asya.areaCode },
            name = "İstanbul",
            path = combinedPath,
            bounds = bounds
        )

        return provinces
            .filter { it.id !in setOf("istanbul-asya", "istanbul-avrupa") }
            .plus(istanbul)
    }

    private fun parseViewBox(html: String): RectF? {
        val values = VIEW_BOX_REGEX.find(html)?.groupValues?.getOrNull(1)
            ?.trim()
            ?.split(Regex("\\s+"))
            ?.mapNotNull { it.toFloatOrNull() }
            ?: return null

        if (values.size != 4) return null
        return RectF(values[0], values[1], values[0] + values[2], values[1] + values[3])
    }

    private fun addProvinceOrNull(
        id: String,
        plateCode: String,
        areaCode: String,
        name: String,
        groupContent: String
    ): TurkeyProvince? {
        if (id.isBlank() || plateCode.isBlank()) return null

        val combinedPath = Path()
        var hasPath = false

        PATH_DATA_REGEX.findAll(groupContent).forEach { pathMatch ->
            val pathData = pathMatch.groupValues[1].trim()
            if (pathData.isBlank()) return@forEach
            runCatching {
                PathParser.createPathFromPathData(pathData)
            }.getOrNull()?.let { parsedPath ->
                combinedPath.addPath(parsedPath)
                hasPath = true
            }
        }

        if (!hasPath) return null

        val bounds = RectF()
        combinedPath.computeBounds(bounds, true)
        if (bounds.isEmpty) return null

        return TurkeyProvince(
            id = id,
            plateCode = plateCode.trim(),
            areaCode = areaCode.trim(),
            name = decodeHtmlEntities(name.trim()),
            path = combinedPath,
            bounds = bounds
        )
    }

    private fun decodeHtmlEntities(value: String): String = buildString {
        var index = 0
        while (index < value.length) {
            if (value[index] == '&') {
                val semicolonIndex = value.indexOf(';', index + 1)
                if (semicolonIndex != -1) {
                    val entity = value.substring(index, semicolonIndex + 1)
                    val decoded = decodeEntity(entity)
                    if (decoded != null) {
                        append(decoded)
                        index = semicolonIndex + 1
                        continue
                    }
                }
            }
            append(value[index])
            index++
        }
    }

    private fun decodeEntity(entity: String): String? = when (entity) {
        "&amp;" -> "&"
        "&lt;" -> "<"
        "&gt;" -> ">"
        "&quot;" -> "\""
        "&apos;" -> "'"
        else -> {
            if (entity.startsWith("&#x", ignoreCase = true) && entity.endsWith(";")) {
                entity.substring(3, entity.length - 1).toIntOrNull(16)?.toChar()?.toString()
            } else if (entity.startsWith("&#") && entity.endsWith(";")) {
                entity.substring(2, entity.length - 1).toIntOrNull()?.toChar()?.toString()
            } else {
                null
            }
        }
    }
}

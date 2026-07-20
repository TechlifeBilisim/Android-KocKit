package com.techlife.kockit.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object ProfileImageCodec {
    private const val MAX_SIDE_PX = 720
    private const val JPEG_QUALITY = 80

    fun encodeUriToBase64(context: Context, uri: Uri): String? {
        val original = context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input)
        } ?: return null
        val scaled = scaleDown(original)
        val bytes = ByteArrayOutputStream().use { output ->
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
            output.toByteArray()
        }
        if (scaled !== original) {
            scaled.recycle()
        }
        original.recycle()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun toCoilModel(resim: String?): Any? {
        val sanitized = sanitizeProfileImage(resim) ?: return null
        return when {
            sanitized.startsWith("http://", ignoreCase = true) ||
                sanitized.startsWith("https://", ignoreCase = true) ||
                sanitized.startsWith("content://", ignoreCase = true) ||
                sanitized.startsWith("file://", ignoreCase = true) -> sanitized
            sanitized.startsWith("data:image", ignoreCase = true) -> sanitized
            else -> runCatching {
                Base64.decode(sanitized, Base64.DEFAULT)
            }.getOrNull()?.takeIf { it.isNotEmpty() }
        }
    }

    fun sanitizeProfileImage(resim: String?): String? =
        resim?.takeIf { it.isNotBlank() && !it.equals("string", ignoreCase = true) }

    private fun scaleDown(bitmap: Bitmap): Bitmap {
        val maxSide = maxOf(bitmap.width, bitmap.height)
        if (maxSide <= MAX_SIDE_PX) return bitmap
        val ratio = MAX_SIDE_PX.toFloat() / maxSide
        val width = (bitmap.width * ratio).toInt().coerceAtLeast(1)
        val height = (bitmap.height * ratio).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}

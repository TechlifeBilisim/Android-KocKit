package com.techlife.kockit.core.network.interceptor

import android.util.Log
import com.techlife.kockit.core.network.annotation.ApiLog
import com.techlife.kockit.core.network.config.NetworkConfig
import com.techlife.kockit.core.network.util.JsonFormatter
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import retrofit2.Invocation
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KocKitLoggingInterceptor @Inject constructor(
    private val jsonFormatter: JsonFormatter,
    private val isLoggingEnabled: Boolean
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isLoggingEnabled) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()
        val serviceName = resolveServiceName(request)
        val logTag = "${NetworkConfig.LOG_TAG_ROOT}/$serviceName"
        val startedAtNs = System.nanoTime()

        logBlock(
            tag = logTag,
            lines = buildRequestLogLines(request, serviceName)
        )

        return try {
            val response = chain.proceed(request)
            val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNs)
            logBlock(
                tag = logTag,
                lines = buildResponseLogLines(request, response, serviceName, durationMs)
            )
            response
        } catch (throwable: Throwable) {
            val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNs)
            logBlock(
                tag = logTag,
                lines = buildFailureLogLines(request, serviceName, throwable, durationMs)
            )
            throw throwable
        }
    }

    private fun resolveServiceName(request: Request): String {
        val annotatedName = request.tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(ApiLog::class.java)
            ?.service
        if (!annotatedName.isNullOrBlank()) return annotatedName

        return request.url.encodedPath
            .trim('/')
            .replace('/', '_')
            .uppercase()
            .ifBlank { "UNKNOWN" }
    }

    private fun buildRequestLogLines(request: Request, serviceName: String): List<String> {
        val token = extractToken(request.header(NetworkConfig.HEADER_AUTHORIZATION))
        return listOf(
            "╔══════════════════════════════════════════════════════════════",
            "║  KocKit API  ▸  $serviceName",
            "╠══════════════════════════════════════════════════════════════",
            "║  ${request.method}  ${request.url}",
            "╠────────────────────── TOKEN ───────────────────────────────",
            "║  $token",
            "╠────────────────────── HEADERS ───────────────────────────────",
            *formatHeaders(request),
            "╠────────────────────── REQUEST BODY ────────────────────────",
            "║  ${formatBody(readRequestBody(request))}",
            "╚══════════════════════════════════════════════════════════════"
        )
    }

    private fun buildResponseLogLines(
        request: Request,
        response: Response,
        serviceName: String,
        durationMs: Long
    ): List<String> {
        val token = extractToken(request.header(NetworkConfig.HEADER_AUTHORIZATION))
        val responseBody = response.peekBody(MAX_LOG_BODY_BYTES).string()
        return listOf(
            "╔══════════════════════════════════════════════════════════════",
            "║  KocKit API  ▸  $serviceName  ▸  ${response.code} ${response.message}",
            "╠══════════════════════════════════════════════════════════════",
            "║  ${request.method}  ${request.url}",
            "║  Süre: ${durationMs}ms",
            "╠────────────────────── TOKEN ───────────────────────────────",
            "║  $token",
            "╠────────────────────── RESPONSE BODY ───────────────────────",
            "║  ${formatBody(responseBody)}",
            "╚══════════════════════════════════════════════════════════════"
        )
    }

    private fun buildFailureLogLines(
        request: Request,
        serviceName: String,
        throwable: Throwable,
        durationMs: Long
    ): List<String> {
        val token = extractToken(request.header(NetworkConfig.HEADER_AUTHORIZATION))
        return listOf(
            "╔══════════════════════════════════════════════════════════════",
            "║  KocKit API  ▸  $serviceName  ▸  FAILED",
            "╠══════════════════════════════════════════════════════════════",
            "║  ${request.method}  ${request.url}",
            "║  Süre: ${durationMs}ms",
            "╠────────────────────── TOKEN ───────────────────────────────",
            "║  $token",
            "╠────────────────────── ERROR ─────────────────────────────────",
            "║  ${throwable::class.simpleName}: ${throwable.message ?: "—"}",
            "╚══════════════════════════════════════════════════════════════"
        )
    }

    private fun formatHeaders(request: Request): Array<String> {
        if (request.headers.size == 0) {
            return arrayOf("║  —")
        }
        return request.headers.names()
            .flatMap { name ->
                request.headers(name).map { value ->
                    val displayValue = if (name.equals(NetworkConfig.HEADER_AUTHORIZATION, ignoreCase = true)) {
                        maskToken(value)
                    } else {
                        value
                    }
                    "║  $name: $displayValue"
                }
            }
            .toTypedArray()
    }

    private fun readRequestBody(request: Request): String? {
        val body = request.body ?: return null
        return runCatching {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        }.getOrNull()
    }

    private fun formatBody(raw: String?): String {
        if (raw.isNullOrBlank()) return "—"
        return jsonFormatter.format(raw)
            .lines()
            .joinToString(separator = "\n║  ") { it }
    }

    private fun extractToken(authorizationHeader: String?): String {
        if (authorizationHeader.isNullOrBlank()) return "—"
        val token = authorizationHeader.removePrefix(NetworkConfig.BEARER_PREFIX).trim()
        if (token.isBlank()) return "—"
        return if (isLoggingEnabled) {
            token
        } else {
            maskTokenForRelease(token)
        }
    }

    private fun maskTokenForRelease(token: String): String {
        return if (token.length <= 12) {
            token
        } else {
            "${token.take(8)}...${token.takeLast(6)}  (len=${token.length})"
        }
    }

    private fun maskToken(authorizationHeader: String): String {
        val token = authorizationHeader.removePrefix(NetworkConfig.BEARER_PREFIX).trim()
        if (token.isBlank()) return "—"
        return if (isLoggingEnabled) token else maskTokenForRelease(token)
    }

    private fun logBlock(tag: String, lines: List<String>) {
        lines.forEach { line -> Log.i(tag, line) }
    }

    private companion object {
        const val MAX_LOG_BODY_BYTES = 256_000L
    }
}

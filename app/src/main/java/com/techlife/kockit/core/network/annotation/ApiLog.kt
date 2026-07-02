package com.techlife.kockit.core.network.annotation

/**
 * Retrofit servis metoduna eklenir. Logcat'te her servis kendi tag'i ile görünür.
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ApiLog(val service: String)

object ApiServices {
    const val AUTH_LOGIN = "AUTH_LOGIN"
    const val AUTH_REGISTER = "AUTH_REGISTER"
    const val LESSON_LIST = "LESSON_LIST"
    const val PROVINCE_LIST = "PROVINCE_LIST"
    const val DISTRICT_LIST = "DISTRICT_LIST"
}

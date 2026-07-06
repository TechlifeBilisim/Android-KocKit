package com.techlife.kockit.core.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.techlife.kockit.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object GoogleSignInHelper {

    fun webClientId(context: Context): String {
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()) {
            return BuildConfig.GOOGLE_WEB_CLIENT_ID
        }
        val resourceId = context.resources.getIdentifier(
            "default_web_client_id",
            "string",
            context.packageName
        )
        if (resourceId != 0) {
            return context.getString(resourceId)
        }
        return ""
    }

    fun isConfigured(context: Context): Boolean = webClientId(context).isNotBlank()

    fun client(context: Context): GoogleSignInClient {
        val webClientId = webClientId(context)
        require(webClientId.isNotBlank()) {
            "Google Web Client ID is not configured."
        }
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    suspend fun createSignInIntent(context: Context): Intent {
        val googleClient = client(context)
        return suspendCancellableCoroutine { continuation ->
            googleClient.signOut().addOnCompleteListener {
                if (continuation.isActive) {
                    continuation.resume(googleClient.signInIntent)
                }
            }
        }
    }

    fun configurationErrorMessage(): String =
        "Google girişi yapılandırılmamış. GOOGLE_WEB_CLIENT_ID ekleyin veya google-services.json kullanın."

    fun parseSignInResult(resultCode: Int, data: Intent?): GoogleSignInOutcome {
        if (resultCode == Activity.RESULT_CANCELED) {
            return GoogleSignInOutcome.Cancelled
        }
        return try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)
            GoogleSignInOutcome.Success(account)
        } catch (exception: ApiException) {
            GoogleSignInOutcome.Error(errorMessage(exception))
        } catch (_: Exception) {
            GoogleSignInOutcome.Error("Google hesabı seçilemedi.")
        }
    }

    private fun errorMessage(exception: ApiException): String = when (exception.statusCode) {
        CommonStatusCodes.DEVELOPER_ERROR ->
            "Google giriş yapılandırması hatalı. SHA-1 ve Web Client ID ayarlarını kontrol edin."
        CommonStatusCodes.NETWORK_ERROR -> "Ağ hatası. Tekrar deneyin."
        else -> "Google hesabı seçilemedi."
    }
}

sealed interface GoogleSignInOutcome {
    data class Success(val account: GoogleSignInAccount) : GoogleSignInOutcome
    data object Cancelled : GoogleSignInOutcome
    data class Error(val message: String) : GoogleSignInOutcome
}

package com.example.smartglass.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.smartglass.R
import com.example.smartglass.data.model.GoogleSignInResult
import com.example.smartglass.data.model.UserDataFromGoogleAccount
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {


    private val auth = Firebase.auth
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): GoogleSignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            GoogleSignInResult(
                data = user?.run {
                    email?.let {
                        UserDataFromGoogleAccount(
                            userId = uid,
                            username = displayName,
                            email = it

                        )
                    }

                },
                errorMessage = null
            )

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            GoogleSignInResult(
                data = null,
                errorMessage = e.message

            )
        }

    }

    fun signOut() {
        try {
            oneTapClient.signOut()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserDataFromGoogleAccount? = auth.currentUser?.run {
        if (providerId != GoogleAuthProvider.PROVIDER_ID && email != null) {
            UserDataFromGoogleAccount(
                userId = uid,
                username = displayName,
                email = email!!
            )
        } else {
            null
        }
    }


    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.webClientID))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}
package com.vyou.android.compose.sample.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.channels.Channel

class GoogleSignInCollaborator(context: Context, serverClientId: String) {
    private val gso = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(serverClientId)
        .requestEmail()
        .build()

    private val client = GoogleSignIn.getClient(context, gso)

    private val channel = Channel<Result<String>>()

    private lateinit var launcher: ActivityResultLauncher<Unit>

    suspend fun signIn(): Result<String> {
        launcher.launch(Unit)
        return channel.receive()
    }

    fun signOut() {
        client.signOut()
    }

    fun registerForActivityResult(activityResultCaller: ActivityResultCaller) {
        launcher = activityResultCaller.registerForActivityResult(getContract()) { token ->
            if(token.isNotEmpty()) {
                channel.trySend(Result.success(token))
            } else {
                channel.trySend(Result.failure(Error("Google login cancelled")))
            }
        }
    }

    private fun getContract() = object : ActivityResultContract<Unit, String>() {
        override fun createIntent(context: Context, input: Unit): Intent = client.signInIntent

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            return runCatching {
                GoogleSignIn.getSignedInAccountFromIntent(intent).result?.idToken ?: ""
            }.onFailure {
                channel.trySend(Result.failure(Error(it.message)))
            }.getOrElse { "" }
        }
    }
}


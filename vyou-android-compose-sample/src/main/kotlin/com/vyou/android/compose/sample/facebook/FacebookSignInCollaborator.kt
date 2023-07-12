package com.vyou.android.compose.sample.facebook

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.channels.Channel

class FacebookSignInCollaborator(clientId: String) {
    private val loginManager = LoginManager.getInstance()
    private val callback = CallbackManager.Factory.create()
    private val channel = Channel<Result<String>>()
    private val permissions = listOf("email","public_profile")

    init {
        FacebookSdk.setApplicationId(clientId)
    }

    suspend fun signIn(activity: Activity): Result<String> = signIn {
        loginManager.logInWithReadPermissions(activity, permissions)
    }

    suspend fun signIn(fragment: Fragment): Result<String> = signIn {
        loginManager.logInWithReadPermissions(fragment, permissions)
    }

    private suspend fun signIn(block: () -> Unit): Result<String> {
        loginManager.registerCallback(callback, MyFacebookCallback())
        block()
        return channel.receive()
    }

    fun signOut() {
        loginManager.logOut()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callback.onActivityResult(requestCode, resultCode, data)
    }

    private inner class MyFacebookCallback: FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            channel.trySend(Result.success(result.accessToken.token))
        }

        override fun onCancel() {
            channel.trySend(Result.failure(Error("Facebook login cancelled by user")))
        }

        override fun onError(error: FacebookException) {
            channel.trySend(Result.failure(Error(error.message ?: "Facebook login throw an exception")))
        }
    }
}
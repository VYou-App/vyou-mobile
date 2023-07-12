package com.vyou.android.compose.sample.facebook

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.fragment.app.Fragment
import com.facebook.FacebookSdk
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class FacebookService(
    private val signInCollaborator: FacebookSignInCollaborator
){

    suspend fun getAccessToken(activity: Activity): String = signInCollaborator.signIn(activity).getOrThrow()

    suspend fun getAccessToken(fragment: Fragment): String = signInCollaborator.signIn(fragment).getOrThrow()

    fun signOut() {
        signInCollaborator.signOut()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        signInCollaborator.onActivityResult(requestCode, resultCode, data)
    }
}
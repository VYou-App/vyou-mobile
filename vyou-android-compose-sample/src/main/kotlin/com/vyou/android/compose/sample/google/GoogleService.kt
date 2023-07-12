package com.vyou.android.compose.sample.google

import androidx.activity.result.ActivityResultCaller
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject


class GoogleService(
    private val signInCollaborator: GoogleSignInCollaborator
) {
    /**
     * Initialize [GoogleSignInCollaborator] components and register activity to internal contract.
     * Must be invoked before the onStart method inside your activity or fragment class.
     *
     * @param activityResultCaller Used to register activity with ActivityResultLauncher.
     */
    fun registerForActivityResult(activityResultCaller: ActivityResultCaller) {
        signInCollaborator.registerForActivityResult(activityResultCaller)
    }

    suspend fun getAccessToken(): String = signInCollaborator.signIn().getOrThrow()

    fun signOut() {
        signInCollaborator.signOut()
    }
}
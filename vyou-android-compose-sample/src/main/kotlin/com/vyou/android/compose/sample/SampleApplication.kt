package com.vyou.android.compose.sample

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import com.facebook.FacebookSdk
import com.vyou.android.compose.sample.di.appModule
import com.vyou.android.compose.sample.di.facebookModule
import com.vyou.android.compose.sample.di.googleModule
import com.vyou.android.compose.sample.feature.MainActivity
import com.vyou.android.compose.sample.facebook.FacebookService
import com.vyou.android.compose.sample.google.GoogleService
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouLogLevel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class SampleApplication : Application() {
    private val googleService by inject<GoogleService>()
    private val facebookService by inject<FacebookService>()

    override fun onCreate() {
        super.onCreate()
        initFacebook()
        initVYou()

    }

    private fun initVYou() {
        VYou.Builder(
            clientId = getString(R.string.vyou_client_id),
            serverUrl = getString(R.string.vyou_server_url),
            publicSaltBase64 = getString(R.string.vyou_public_salt)
        ) {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@SampleApplication)
        }
            .addModules(listOf(googleModule, facebookModule, appModule))
            .addOnRefreshTokenFailure {
                val uri = "${getString(R.string.vyou_client_uri_scheme)}://${getString(R.string.vyou_client_uri_host)}${getString(R.string.vyou_client_uri_path_pattern)}"
                val intent = Intent(Intent.ACTION_VIEW, "${uri}login".toUri(), applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .addOnSignOut {
                googleService.signOut()
                facebookService.signOut()
            }
            .enableNetworkLogs(VYouLogLevel.ALL)
            .initialize()
    }

    private fun initFacebook() {
        FacebookSdk.setApplicationId(getString(R.string.vyou_facebook_client_id))
        FacebookSdk.sdkInitialize(this)
    }
}
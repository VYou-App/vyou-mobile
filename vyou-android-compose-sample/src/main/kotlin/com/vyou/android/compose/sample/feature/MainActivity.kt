package com.vyou.android.compose.sample.feature

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.vyou.android.compose.sample.common.ui.AppTheme
import com.vyou.android.compose.sample.R
import com.vyou.android.compose.sample.facebook.FacebookService
import com.vyou.android.compose.sample.google.GoogleService
import com.vyou.kmm.client.VYou
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val googleService: GoogleService by inject()
    private val facebookService: FacebookService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleService.registerForActivityResult(this)

        setContent {
            MainLayout(startDestination(), deeplinkUri())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookService.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startDestination(): String = if (VYou.instance().isLoggedIn()) Screen.Profile.title else Screen.Login.title

    private fun deeplinkUri(): String {
        val scheme = getString(R.string.vyou_client_uri_scheme)
        val host = getString(R.string.vyou_client_uri_host)
        val pathPattern = getString(R.string.vyou_client_uri_path_pattern)
        return "$scheme://$host$pathPattern"
    }
}

@Composable
fun MainLayout(startDestination: String, deeplinkUri: String) {
    val navController = rememberNavController()
    AppTheme {
        Scaffold(Modifier.padding(16.dp)) { paddingValues ->
            MainNavHost(navController = navController, startDestination = startDestination, deeplinkUri = deeplinkUri, paddingValues)
        }
    }
}
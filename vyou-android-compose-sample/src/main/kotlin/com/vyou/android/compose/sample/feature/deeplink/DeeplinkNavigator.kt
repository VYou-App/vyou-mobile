package com.vyou.android.compose.sample.feature.deeplink

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class DeeplinkNavigator(navController: NavController): BaseNavigator(navController) {
    fun navigateToLogin() {
        navigateTop(Screen.Login)
    }

    fun navigateToPassword(code: String) {
        navigateTop(Screen.RegisterPassword, "/$code")
    }
}
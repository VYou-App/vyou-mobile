package com.vyou.android.compose.sample.feature.verify

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class VerifyNavigator(navController: NavController) : BaseNavigator(navController) {
    fun navigateToRegisterPassword(code: String) {
        navigate(Screen.RegisterPassword, "/$code")
    }
}
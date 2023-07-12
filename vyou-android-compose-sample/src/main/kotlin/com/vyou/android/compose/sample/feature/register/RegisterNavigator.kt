package com.vyou.android.compose.sample.feature.register

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class RegisterNavigator(navController: NavController) : BaseNavigator(navController) {
    fun navigateToLogin() {
        navigateTop(Screen.Login)
    }

    fun navigateToVerify(email: String) {
        navigate(Screen.Verify, "/$email")
    }
}
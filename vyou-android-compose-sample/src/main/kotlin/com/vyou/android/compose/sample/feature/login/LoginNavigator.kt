package com.vyou.android.compose.sample.feature.login

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class LoginNavigator(navController: NavController): BaseNavigator(navController) {
    fun navigateToRegister() {
        navigateTop(Screen.Register)
    }

    fun navigateToResetPassword() {
        navigate(Screen.ResetPassword)
    }

    fun navigateToMain() {
        navigateTop(Screen.Profile)
    }
}
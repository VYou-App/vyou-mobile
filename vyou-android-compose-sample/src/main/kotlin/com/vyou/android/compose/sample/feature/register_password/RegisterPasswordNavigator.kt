package com.vyou.android.compose.sample.feature.register_password

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class RegisterPasswordNavigator(navController: NavController) : BaseNavigator(navController) {
    fun navigateToLogin() {
        navigateTop(Screen.Login)
    }
}
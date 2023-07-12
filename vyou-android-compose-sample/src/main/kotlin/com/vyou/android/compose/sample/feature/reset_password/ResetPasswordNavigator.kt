package com.vyou.android.compose.sample.feature.reset_password

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.android.compose.sample.common.ui.BaseNavigator

class ResetPasswordNavigator(navController: NavController) : BaseNavigator(navController) {
    fun navigateToVerify(email: String) {
        navigate(Screen.Verify, "/$email")
    }
}
package com.vyou.android.compose.sample.feature.edit_profile

import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.BaseNavigator
import com.vyou.android.compose.sample.feature.Screen

class EditProfileNavigator(navController: NavController): BaseNavigator(navController) {
    fun navigateToProfile() = navigateTop(Screen.Profile)
}
package com.vyou.android.compose.sample.feature.profile

import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.BaseNavigator
import com.vyou.android.compose.sample.feature.Screen
import com.vyou.kmm.client.VYouField
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileNavigator(navController: NavController) : BaseNavigator(navController) {
    fun editProfile(fields: List<VYouField>) {
        val json = Json.encodeToString(fields)
        navigate(Screen.EditProfile, "/$json")
    }

    fun logOut() = navigateTop(Screen.Login)
}
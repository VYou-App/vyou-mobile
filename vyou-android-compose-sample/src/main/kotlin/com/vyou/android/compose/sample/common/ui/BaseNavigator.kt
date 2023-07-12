package com.vyou.android.compose.sample.common.ui

import androidx.navigation.NavController
import com.vyou.android.compose.sample.feature.Screen

abstract class BaseNavigator(private val navController: NavController) {
    protected fun navigateTop(screen: Screen, path: String = "") {
        navController.navigate(screen.title+path) {
            popUpTo(navController.graph.id)
            launchSingleTop = true
        }
    }

    protected fun navigate(screen: Screen, path: String = "") {
        navController.navigate(screen.title+path)
    }

    fun popBack() {
        navController.popBackStack()
    }
}
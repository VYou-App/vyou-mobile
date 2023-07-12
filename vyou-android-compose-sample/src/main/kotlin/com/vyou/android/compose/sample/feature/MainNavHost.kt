package com.vyou.android.compose.sample.feature

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.vyou.android.compose.sample.feature.deeplink.DeeplinkScreen
import com.vyou.android.compose.sample.feature.edit_profile.EditProfileScreen
import com.vyou.android.compose.sample.feature.login.LoginScreen
import com.vyou.android.compose.sample.feature.profile.ProfileScreen
import com.vyou.android.compose.sample.feature.register.RegisterScreen
import com.vyou.android.compose.sample.feature.register_password.RegisterPasswordScreen
import com.vyou.android.compose.sample.feature.reset_password.ResetPasswordScreen
import com.vyou.android.compose.sample.feature.verify.VerifyScreen
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class Screen(val title: String) {
    object Login : Screen("Login")
    object ResetPassword : Screen("ResetPassword")
    object Register : Screen("Register")
    object Verify : Screen("Verify")
    object RegisterPassword : Screen("RegisterPassword")
    object Profile : Screen("Profile")
    object EditProfile : Screen("EditProfile")
    object Deeplink : Screen("Deeplink")
}

@Composable
fun MainNavHost(navController: NavHostController, startDestination: String, deeplinkUri: String, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = Screen.Login.title,
            deepLinks = listOf(navDeepLink {
                uriPattern = "$deeplinkUri/login"
            })
        ) {
            LoginScreen(paddingValues, navController)
        }
        composable(
            route = Screen.Register.title
        ) {
            RegisterScreen(paddingValues, navController)
        }
        composable(
            route = Screen.ResetPassword.title
        ) {
            ResetPasswordScreen(paddingValues, navController)
        }
        composable(
            route = "${Screen.Verify.title}/{email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyScreen(email, paddingValues, navController)
        }
        composable(
            route = "${Screen.RegisterPassword.title}/{code}",
            arguments = listOf(navArgument("code") {
                type = NavType.StringType
            })
        ) {
            RegisterPasswordScreen(paddingValues, navController)
        }
        composable(
            route = Screen.Profile.title
        ) {
            ProfileScreen(paddingValues, navController)
        }
        composable(
            route = "${Screen.EditProfile.title}/{fields}",
            arguments = listOf(navArgument("fields") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val jsonFields = backStackEntry.arguments?.getString("fields") ?: ""
            EditProfileScreen(Json.decodeFromString(jsonFields), paddingValues, navController)
        }
        composable(
            route = "${Screen.Deeplink.title}/{code}",
            deepLinks = listOf(navDeepLink {
                uriPattern = "$deeplinkUri?token={code}"
            })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            DeeplinkScreen(code, navController)
        }
    }
}
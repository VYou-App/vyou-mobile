package com.vyou.android.compose.sample.feature.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DeeplinkScreen(code: String, navController: NavController, viewModel: DeeplinkViewModel = koinViewModel { parametersOf(code, DeeplinkNavigator(navController)) }) {
    LaunchedEffect(key1 = DeeplinkViewModel.KEY_LAUNCHED_EFFECT) {
        viewModel.verify()
    }
}
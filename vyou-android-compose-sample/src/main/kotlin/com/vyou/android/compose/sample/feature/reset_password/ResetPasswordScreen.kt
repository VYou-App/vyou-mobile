package com.vyou.android.compose.sample.feature.reset_password

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.EmailTextField
import com.vyou.android.compose.sample.common.ui.TextHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResetPasswordScreen(paddingValues: PaddingValues, navController: NavController, viewModel: ResetPasswordViewModel = koinViewModel { parametersOf(ResetPasswordNavigator(navController)) }) {
    val state = viewModel.state.collectAsState().value
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextHeader(text = "Reset password")
            Spacer(modifier = Modifier.size(16.dp))
            EmailTextField(email = state.email, onValueChange = viewModel::emailChanged)
        }

        Button(onClick = {
            viewModel.resetPassword()
        }, enabled = state.ctaEnabled, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)) {
            Text(text = "Send".uppercase())
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    LaunchedEffect(state.error) {
        if(state.error?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.error)
        }
    }
}
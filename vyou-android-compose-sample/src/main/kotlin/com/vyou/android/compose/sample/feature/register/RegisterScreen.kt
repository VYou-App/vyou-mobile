package com.vyou.android.compose.sample.feature.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.CheckboxText
import com.vyou.android.compose.sample.common.ui.EmailTextField
import com.vyou.android.compose.sample.common.ui.LoadingButton
import com.vyou.android.compose.sample.common.ui.TextHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RegisterScreen(paddingValues: PaddingValues, navController: NavController, viewModel: RegisterViewModel = koinViewModel { parametersOf(RegisterNavigator(navController)) }) {
    val state = viewModel.state.collectAsState().value
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextHeader(text = "Register")
            Spacer(modifier = Modifier.size(16.dp))
            EmailTextField(email = state.email, error = state.emailError, onValueChange = viewModel::emailChanged)
            Spacer(modifier = Modifier.size(16.dp))
            CheckboxText(checked = state.termsAndConditions, text = "Accept terms and conditions", onCheckedChange = viewModel::termsAndConditionsChanged)
            Spacer(modifier = Modifier.size(16.dp))
            CheckboxText(checked = state.privacyPolicy, text = "Accept privacy policy", onCheckedChange = viewModel::privacyPolicyChanged)
            Spacer(modifier = Modifier.size(16.dp))
            CheckboxText(checked = state.info, text = "Accept ads and info", onCheckedChange = viewModel::infoChanged)
        }

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            LoadingButton(label = "Sign up", isLoading = state.loading, isEnabled = state.signUpEnabled) {
                viewModel.register()
            }
            OutlinedButton(onClick = {
                viewModel.logIn()
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Do you have an account? Log in".uppercase())
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))

        LaunchedEffect(state.error) {
            if(state.error?.isNotEmpty() == true) {
                snackbarHostState.showSnackbar(state.error)
            }
        }
    }
}
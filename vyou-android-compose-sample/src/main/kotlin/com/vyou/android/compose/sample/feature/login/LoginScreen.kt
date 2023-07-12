package com.vyou.android.compose.sample.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ext.getActivity
import com.vyou.android.compose.sample.common.ui.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LoginScreen(paddingValues: PaddingValues, navController: NavController, viewModel: LoginViewModel = koinViewModel { parametersOf(LoginNavigator(navController)) }) {
    val state = viewModel.state.collectAsState().value
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val context = LocalContext.current

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextHeader(text = "Login")
            EmailTextField(
                email = state.email,
                error = state.emailError,
                onValueChange = viewModel::emailChanged
            )
            PasswordTextField(
                password = state.password,
                passwordVisibility = state.passwordVisible,
                error = state.passwordError,
                onPasswordVisibilityChange = viewModel::passwordVisibilityChanged,
                onValueChange = viewModel::passwordChanged
            )
            TextLink { viewModel.forgotPassword() }
            LoadingButton("Log in", state.signInLoading, state.logInEnabled) {
                viewModel.signIn(state.email, state.password)
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            LoadingButton(label = "Sign in with Google", isLoading = state.signInWithGoogleLoading, isEnabled = true) {
                viewModel.signInWithGoogle()
            }
            LoadingButton(label = "Sign in with Facebook", isLoading = state.signInWithFacebookLoading, isEnabled = true) {
                context.getActivity()?.let { activity ->
                    viewModel.signInWithFacebook(activity)
                }
            }
        }

        OutlinedButton(onClick = {
            viewModel.createAccount()
        }, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)) {
            Text(text = "Create an account".uppercase())
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
    
    LaunchedEffect(state.error) {
        if(state.error?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.error)
        }
    }
}
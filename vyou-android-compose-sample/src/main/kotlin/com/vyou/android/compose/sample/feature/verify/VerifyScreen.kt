package com.vyou.android.compose.sample.feature.verify

import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.BasicTextField
import com.vyou.android.compose.sample.common.ui.LoadingButton
import com.vyou.android.compose.sample.common.ui.LoadingOutlinedButton
import com.vyou.android.compose.sample.common.ui.TextHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VerifyScreen(email: String, paddingValues: PaddingValues, navController: NavController, viewModel: VerifyViewModel = koinViewModel { parametersOf(email, VerifyNavigator(navController)) }) {
    VerifyComponent(
        state = viewModel.state.collectAsState().value,
        paddingValues = paddingValues,
        updateCode = viewModel::updateCode,
        verify = viewModel::verify,
        resendPassword = viewModel::resendPassword
    )
}

@Composable
fun VerifyComponent(
    state: VerifyState,
    paddingValues: PaddingValues,
    updateCode: (String) -> Unit,
    verify: () -> Unit,
    resendPassword: () -> Unit
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
        Column(Modifier.fillMaxWidth()) {
            TextHeader(text = "Verify register")
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Introduce the code that we send you to your email")
            Spacer(modifier = Modifier.size(8.dp))
            BasicTextField(value = state.code, label = "Code", error = state.codeError, onValueChange = updateCode)
        }


        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
            LoadingButton("Send code", state.sendLoading, state.code.isNotEmpty(), func = verify)
            LoadingOutlinedButton("Resend email", state.resendLoading, true, func = resendPassword)
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    LaunchedEffect(state.snackbarMessage) {
        if(state.snackbarMessage?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.snackbarMessage)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun VerifyComponentPreview() {
    VerifyComponent(
        state = VerifyState(email = "email@email.com"),
        paddingValues = PaddingValues(),
        updateCode = {},
        verify = {},
        resendPassword = {}
    )
}
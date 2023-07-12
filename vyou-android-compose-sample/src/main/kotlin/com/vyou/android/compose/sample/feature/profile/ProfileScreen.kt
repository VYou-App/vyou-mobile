package com.vyou.android.compose.sample.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.LoadingButton
import com.vyou.android.compose.sample.common.ui.LoadingOutlinedButton
import com.vyou.android.compose.sample.common.ui.TextHeader
import com.vyou.android.compose.sample.common.ui.TextSubtitle
import com.vyou.kmm.client.VYouFieldDate
import com.vyou.kmm.client.VYouFieldEmail
import com.vyou.kmm.client.VYouFieldNumber
import com.vyou.kmm.client.VYouFieldText
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileScreen(paddingValues: PaddingValues, navController: NavController, viewModel: ProfileViewModel = koinViewModel{ parametersOf(ProfileNavigator(navController)) }) {
    val state = viewModel.state.collectAsState().value
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxWidth()) {
            val profile = state.profile
            TextHeader(text = profile?.email ?: "")
            Spacer(modifier = Modifier.size(8.dp))
            LazyColumn {
                items(profile?.fields?.toList() ?: emptyList()) { item ->
                    TextSubtitle(item.name.replaceFirstChar { it.uppercase() })
                    when (item) {
                        is VYouFieldNumber -> Text(item.value?.toString() ?: "0")
                        is VYouFieldText -> Text(item.value ?: "")
                        is VYouFieldDate -> Text(item.value?.toString() ?: "0")
                        is VYouFieldEmail -> Text(item.value ?: "")
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                }
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            LoadingButton(label = "Edit profile", isLoading = false, isEnabled = true) {
                viewModel.editProfile()
            }
            LoadingOutlinedButton(label = "Log Out", isLoading = state.logOutLoading, isEnabled = true) {
                viewModel.logOut()
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    LaunchedEffect(state.profile) {
        delay(50)
        viewModel.getProfile()
    }

    LaunchedEffect(state.error) {
        if (state.error?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.error)
        }
    }
}
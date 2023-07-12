package com.vyou.android.compose.sample.feature.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vyou.android.compose.sample.common.ui.BasicTextField
import com.vyou.android.compose.sample.common.ui.LoadingButton
import com.vyou.android.compose.sample.common.ui.NumberTextField
import com.vyou.kmm.client.VYouField
import com.vyou.kmm.client.VYouFieldNumber
import com.vyou.kmm.client.VYouFieldText
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditProfileScreen(
    fields: List<VYouField>,
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: EditProfileViewModel = koinViewModel { parametersOf(fields, EditProfileNavigator(navController)) }
) {
    EditProfileComponent(
        state = viewModel.state.collectAsState().value,
        paddingValues = paddingValues,
        updateField = viewModel::updateField,
        save = viewModel::save
    )
}

@Composable
private fun EditProfileComponent(state: EditProfileState, paddingValues: PaddingValues, updateField: (String, Any) -> Unit, save: () -> Unit) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(contentPadding = PaddingValues(bottom = 48.dp)) {
            items(state.fields) { item ->
                when(item) {
                    is VYouFieldNumber -> {
                        NumberTextField(number = item.value ?: 0.0, label = item.name.replaceFirstChar { it.uppercase() }) {
                            updateField(item.name, it)
                        }

                    }
                    is VYouFieldText -> {
                        BasicTextField(value = item.value ?: "", label = item.name.replaceFirstChar { it.uppercase() }) {
                            updateField(item.name, it)
                        }
                    }

                    else -> {}
                }

                Spacer(modifier = Modifier.size(8.dp))
            }
            item {

            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .align(Alignment.BottomCenter)
        ) {
            LoadingButton(label = "Save", isLoading = state.loading, isEnabled = true) {
                save()
            }
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    LaunchedEffect(state.error) {
        if (state.error?.isNotEmpty() == true) {
            snackbarHostState.showSnackbar(state.error)
        }
    }
}
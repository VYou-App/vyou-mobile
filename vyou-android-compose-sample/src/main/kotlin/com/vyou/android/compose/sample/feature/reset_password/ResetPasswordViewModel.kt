package com.vyou.android.compose.sample.feature.reset_password

import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouResetPasswordParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel(private val navigator: ResetPasswordNavigator) : BaseViewModel(navigator) {
    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    fun emailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun resetPassword() = launch {
        _state.update { it.copy(loading = true) }
        runCatching {
            val params = VYouResetPasswordParams(state.value.email)
            VYou.instance().resetPassword(params)
        }.onSuccess {
            _state.update { it.copy(loading = false) }
            navigator.navigateToVerify(state.value.email)
        }.onFailure { error ->
            _state.update { it.copy(loading = false, error = error.message) }
        }
    }
}
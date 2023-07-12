package com.vyou.android.compose.sample.feature.register_password

import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouSignUpPasswordParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterPasswordViewModel(private val navigator: RegisterPasswordNavigator) : BaseViewModel(navigator) {
    private val _state = MutableStateFlow(RegisterPasswordState())
    val state = _state.asStateFlow()

    fun passwordChanged(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun passwordVisibilityChanged() {
        _state.value = _state.value.copy(passwordVisibility = !state.value.passwordVisibility)
    }

    fun registerPassword() = launch {
        _state.update { it.copy(loading = true) }
        runCatching {
            val params = VYouSignUpPasswordParams(state.value.password)
            VYou.instance().signUpPasswords(params)
        }.onSuccess {
            _state.update { it.copy(loading = false) }
            navigator.navigateToLogin()
        }.onFailure { error ->
            _state.update { it.copy(loading = false, error = error.message) }
        }
    }
}
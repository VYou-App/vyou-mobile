package com.vyou.android.compose.sample.feature.register

import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouSignUpParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(private val navigator: RegisterNavigator): BaseViewModel(navigator) {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun emailChanged(email: String) {
        _state.value = state.value.copy(email = email)
    }

    fun termsAndConditionsChanged(termsAndConditions: Boolean) {
        _state.value = state.value.copy(termsAndConditions = termsAndConditions)
    }

    fun privacyPolicyChanged(privacyPolicy: Boolean) {
        _state.value = state.value.copy(privacyPolicy = privacyPolicy)
    }

    fun infoChanged(info: Boolean) {
        _state.value = state.value.copy(info = info)
    }

    fun register() = launch { with(state.value) {
        _state.value = state.value.copy(loading = true)
        runCatching {
            VYou.instance().signUp(VYouSignUpParams(email, termsAndConditions, privacyPolicy, info))
        }.onSuccess {
            _state.value = state.value.copy(loading = false)
            navigator.navigateToVerify(email)
        }.onFailure { error ->
            _state.value = state.value.copy(loading = false, error = error.message)
        }
    } }

    fun logIn() {
        navigator.navigateToLogin()
    }
}
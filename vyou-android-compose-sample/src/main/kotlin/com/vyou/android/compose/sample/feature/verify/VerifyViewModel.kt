package com.vyou.android.compose.sample.feature.verify

import androidx.lifecycle.viewModelScope
import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouSignUpParams
import com.vyou.kmm.client.VYouSignUpVerifyParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class VerifyViewModel(private val email: String, private val navigator: VerifyNavigator) : BaseViewModel(navigator) {
    private val _state = MutableStateFlow(VerifyState(email))
    val state = _state.asStateFlow()

    fun updateCode(code: String) {
        _state.update { it.copy(code = code, codeError = "") }
    }

    fun verify() = launch {
        val code = state.value.code
        _state.update { it.copy(sendLoading = true) }
        runCatching {
            val params = VYouSignUpVerifyParams(code)
            VYou.instance().signUpVerify(params)
        }.onSuccess {
            _state.update { it.copy(sendLoading = false) }
            navigator.navigateToRegisterPassword(code)
        }.onFailure { error ->
            _state.update { it.copy(sendLoading = false) }
            if(error.message?.lowercase()?.contains("code") == true) {
                _state.update { it.copy(codeError = "The code introduced is not valid") }
            }
        }
    }

    fun resendPassword() {
        viewModelScope.launch {
            _state.update { it.copy(resendLoading = true) }
            runCatching {
                val params = VYouSignUpParams(email, termsConditions = true, privacyPolicy = true, info = false)
                VYou.instance().signUp(params)
            }.onSuccess {
                _state.update { it.copy(snackbarMessage = "Check your inbox, to retrieve the latest code") }
            }.onFailure { error ->
                _state.update { it.copy(snackbarMessage = error.message) }
            }
            _state.update { it.copy(resendLoading = false) }
        }
    }
}
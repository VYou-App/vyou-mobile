package com.vyou.android.compose.sample.feature.login

import android.app.Activity
import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.android.compose.sample.facebook.FacebookService
import com.vyou.android.compose.sample.google.GoogleService
import com.vyou.kmm.client.VYou
import com.vyou.kmm.client.VYouSignInProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val googleService: GoogleService,
    private val facebookService: FacebookService,
    private val navigator: LoginNavigator,
) : BaseViewModel(navigator) {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun emailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = "") }
    }

    fun passwordChanged(password: String) {
        _state.update { it.copy(password = password, passwordError = "") }
    }

    fun passwordVisibilityChanged() {
        _state.update { it.copy(passwordVisible = !state.value.passwordVisible) }
    }

    fun signIn(username: String, password: String) = launch {
        _state.update { it.copy(signInLoading = true) }
        runCatching {
            VYou.instance().signIn(VYouSignInProvider.UserPassword(username, password))
        }.onSuccess {
            _state.update { it.copy(signInLoading = false) }
            navigator.navigateToMain()
        }.onFailure { error ->
            if (error.message?.lowercase()?.contains("email") == true) {
                _state.update { it.copy(signInLoading = false, emailError = "The email introduced is not valid") }
            }
            if (error.message?.lowercase()?.contains("password") == true) {
                _state.update { it.copy(signInLoading = false, passwordError = "The password introduced is not valid") }
            }
        }
    }

    fun signInWithGoogle() = launch {
        _state.update { it.copy(signInWithGoogleLoading = true) }
        runCatching {
            val accessToken = googleService.getAccessToken()
            VYou.instance().signIn(VYouSignInProvider.Google(accessToken))
        }.onSuccess {
            _state.update { it.copy(signInWithGoogleLoading = false) }
            navigator.navigateToMain()
        }.onFailure { error ->
            _state.update { it.copy(signInWithGoogleLoading = false, error = error.message) }
        }
    }

    fun signInWithFacebook(activity: Activity) = launch {
        _state.update { it.copy(signInWithFacebookLoading = true) }
        runCatching {
            val accessToken = facebookService.getAccessToken(activity)
            VYou.instance().signIn(VYouSignInProvider.Facebook(accessToken))
        }.onSuccess {
            _state.update { it.copy(signInWithFacebookLoading = false) }
            navigator.navigateToMain()
        }.onFailure { error ->
            _state.update { it.copy(signInWithFacebookLoading = false, error = error.message) }
        }
    }

    fun forgotPassword() {
        navigator.navigateToResetPassword()
    }

    fun createAccount() {
        navigator.navigateToRegister()
    }
}
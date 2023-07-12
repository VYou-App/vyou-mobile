package com.vyou.android.compose.sample.feature.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val emailError: String = "",
    val passwordError: String = "",
    val signInLoading: Boolean = false,
    val signInWithGoogleLoading: Boolean = false,
    val signInWithFacebookLoading: Boolean = false,
    val error: String? = null
) {
    val logInEnabled = email.isNotEmpty() && password.isNotEmpty()
}

package com.vyou.android.compose.sample.feature.register

data class RegisterState(
    val email: String = "",
    val termsAndConditions: Boolean = false,
    val privacyPolicy: Boolean = false,
    val info: Boolean = false,
    val emailError: String = "",
    val loading: Boolean = false,
    val error: String? = null
) {
    val signUpEnabled = email.isNotEmpty() && termsAndConditions && privacyPolicy
}

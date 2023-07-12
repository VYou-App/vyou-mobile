package com.vyou.android.compose.sample.feature.reset_password

data class ResetPasswordState(
    val email: String = "",
    val loading: Boolean = false,
    val error: String? = null
) {
    val ctaEnabled = email.isNotEmpty()
}

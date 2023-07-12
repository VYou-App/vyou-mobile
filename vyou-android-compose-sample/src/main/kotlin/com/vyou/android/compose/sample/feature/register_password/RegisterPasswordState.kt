package com.vyou.android.compose.sample.feature.register_password

data class RegisterPasswordState(
    val password: String = "",
    val passwordVisibility: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) {
    val ctaEnabled = password.isNotEmpty()
}
package com.vyou.android.compose.sample.feature.verify

data class VerifyState(
    val email: String,
    val code: String = "",
    val codeError: String = "",
    val resendLoading: Boolean = false,
    val sendLoading: Boolean = false,
    val snackbarMessage: String? = null
)
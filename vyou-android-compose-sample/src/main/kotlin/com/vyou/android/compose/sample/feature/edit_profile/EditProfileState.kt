package com.vyou.android.compose.sample.feature.edit_profile

import com.vyou.kmm.client.VYouField

data class EditProfileState(
    val fields: List<VYouField>,
    val loading: Boolean = false,
    val error: String? = null
)
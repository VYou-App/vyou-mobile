package com.vyou.android.compose.sample.feature.profile

import com.vyou.kmm.client.VYouProfile

data class ProfileState(
    val profile: VYouProfile? = null,
    val loading: Boolean = false,
    val logOutLoading: Boolean = false,
    val error: String? = null
)

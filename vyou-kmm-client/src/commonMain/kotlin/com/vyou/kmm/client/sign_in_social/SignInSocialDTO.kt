package com.vyou.kmm.client.sign_in_social

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignInSocialDTO(
    @SerialName("token")
    val accessToken: String
)
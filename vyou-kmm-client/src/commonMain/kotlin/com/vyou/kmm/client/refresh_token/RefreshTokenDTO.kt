package com.vyou.kmm.client.refresh_token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshTokenDTO(
    @SerialName("refresh_token")
    val token: String,
    @SerialName("client_id")
    val clientId: String
)
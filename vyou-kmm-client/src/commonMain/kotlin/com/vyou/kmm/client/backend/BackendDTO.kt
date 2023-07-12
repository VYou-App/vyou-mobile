package com.vyou.kmm.client.backend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendDTO(
    @SerialName("salt")
    val salt: String
)

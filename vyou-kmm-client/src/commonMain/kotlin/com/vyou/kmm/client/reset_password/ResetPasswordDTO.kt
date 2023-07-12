package com.vyou.kmm.client.reset_password

import com.vyou.kmm.client.VYouResetPasswordParams
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDTO(
    @SerialName("email")
    val email: String,
    @SerialName("client_id")
    val clientId: String
) {
    companion object {
        fun from(params: VYouResetPasswordParams, clientId: String): ResetPasswordDTO = ResetPasswordDTO(
            email = params.email,
            clientId = clientId
        )
    }
}
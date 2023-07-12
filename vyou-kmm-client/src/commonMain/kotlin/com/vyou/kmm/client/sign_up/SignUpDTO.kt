package com.vyou.kmm.client.sign_up

import com.vyou.kmm.client.VYouSignUpParams
import com.vyou.kmm.client.VYouSignUpPasswordParams
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisterDTO(
    @SerialName("email")
    val email: String,
    @SerialName("terms_conditions_accepted")
    val termsConditionsAccepted: Boolean,
    @SerialName("privacy_accepted")
    val privacyAccepted: Boolean,
    @SerialName("info_accepted")
    val infoAccepted: Boolean,
    @SerialName("client_id")
    val clientId: String
) {
    companion object {
        fun from(data: VYouSignUpParams, clientId: String): RegisterDTO = RegisterDTO(
            email = data.username,
            termsConditionsAccepted = data.termsConditions,
            privacyAccepted = data.privacyPolicy,
            infoAccepted = data.info,
            clientId = clientId
        )
    }
}

@Serializable
internal data class VerifyDTO(
    @SerialName("email")
    val email: String
)

@Serializable
internal data class RegisterPasswordsDTO(
    @SerialName("password")
    val password: String,
    @SerialName("token")
    val token: String
)
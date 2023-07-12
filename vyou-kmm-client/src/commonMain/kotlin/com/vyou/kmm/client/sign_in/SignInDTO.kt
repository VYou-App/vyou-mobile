package com.vyou.kmm.client.sign_in

import com.vyou.kmm.client.VYouCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AuthDTO(
    @SerialName("code_challenge")
    val codeChallenge: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)

@Serializable
internal data class AuthorizeDTO(
    @SerialName("code")
    val code: String
)

@Serializable
internal data class LoginDTO(
    @SerialName("code")
    val code: String,
    @SerialName("code_verifier")
    val codeVerifier: String,
    @SerialName("client_id")
    val clientId: String
)

@Serializable
data class CredentialsDTO(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("id_token")
    val idToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("scope")
    val scope: String,
    /** When false, needs complete profile **/
    @SerialName("tenant_compliant")
    val tenantCompliant: Boolean,
    /** When false, missing legal checkboxes **/
    @SerialName("tenant_consent_compliant")
    val tenantConsentCompliant: Boolean,
    @SerialName("token_type")
    val tokenType: String,
) {
    fun toDomain(): VYouCredentials = VYouCredentials(accessToken, expiresIn, idToken, refreshToken, scope, tenantCompliant, tenantConsentCompliant, tokenType)
}
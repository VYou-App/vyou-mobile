package com.vyou.kmm.client

import io.ktor.client.plugins.logging.*
import kotlinx.serialization.Serializable

data class VYouConfig(
    val serverUrl: String,
    val clientId: String
)

data class VYouError(
    val code: VYouErrorCode,
    override val message: String?,
    val status: Int = 0
) : Error()

enum class VYouErrorCode {
    CREDENTIALS,
    SALT,
    INITIALIZE,
    INSTANCE,
    VALIDATION_PARAMS,
    NETWORK_REQUEST_ERROR,
    NETWORK_SERVER_ERROR,
    NETWORK_REFRESH_TOKEN_EXPIRED,
    NETWORK_ERROR
}

/**
 * Data class to handle credentials used to authenticate calls.
 * @param accessToken JWT token used to validate calls
 * @param expiresIn Date expiration token in epoch seconds
 */
@Serializable
data class VYouCredentials(
    val accessToken: String,
    val expiresIn: Long,
    val idToken: String,
    val refreshToken: String,
    val scope: String,
    val tenantCompliant: Boolean,
    val tenantConsentCompliant: Boolean,
    val tokenType: String
)

sealed class VYouSignInProvider {
    data class UserPassword(val username: String, val password: String) : VYouSignInProvider()
    data class Google(val accessToken: String): VYouSignInProvider()
    data class Facebook(val accessToken: String): VYouSignInProvider()
    data class Apple(val accessToken: String): VYouSignInProvider()
}

data class VYouSignUpParams(
    val username: String,
    val termsConditions: Boolean,
    val privacyPolicy: Boolean,
    val info: Boolean
)

data class VYouSignUpVerifyParams(
    val code: String
)

data class VYouSignUpPasswordParams(
    val password: String
)

data class VYouResetPasswordParams(
    val email: String
)

data class VYouProfile(
    val id: String,
    val email: String,
    val fields: List<VYouField>,
    val tenantCompliant: Boolean,
    val tenantRoles: List<String>
)

@Serializable
sealed interface VYouField {
    val name: String
    val required: Boolean
    val readOnly: Boolean
}

@Serializable
data class VYouFieldText(val value: String?, override val name: String, override val required: Boolean = false, override val readOnly: Boolean = false) : VYouField
@Serializable
data class VYouFieldNumber(val value: Double?, override val name: String, override val required: Boolean = false, override val readOnly: Boolean = false) : VYouField
@Serializable
data class VYouFieldDate(val value: Long?, override val name: String, override val required: Boolean = false, override val readOnly: Boolean = false) : VYouField
@Serializable
data class VYouFieldEmail(val value: String?, override val name: String, override val required: Boolean = false, override val readOnly: Boolean = false) : VYouField

data class VYouEditProfileParams(
    val fields: List<VYouField>
)

enum class VYouLogLevel(val level: LogLevel) {
    ALL(LogLevel.ALL),
    HEADERS(LogLevel.HEADERS),
    BODY(LogLevel.BODY),
    INFO(LogLevel.INFO),
    NONE(LogLevel.NONE)
}
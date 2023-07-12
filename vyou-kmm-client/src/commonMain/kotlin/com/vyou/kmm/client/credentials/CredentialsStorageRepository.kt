package com.vyou.kmm.client.credentials

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CredentialsStorageRepository(private val settings: Settings): CredentialsRepository {

    override var clientId: String
        get() = settings[CLIENT_ID_KEY, ""]
        set(value) {
            settings[CLIENT_ID_KEY] = value
        }

    override var email: String
        get() = settings[EMAIL_KEY, ""]
        set(value) {
            settings[EMAIL_KEY] = value
        }

    override var signUpCode: String
        get() = settings[SIGN_UP_CODE_KEY, ""]
        set(value) {
            settings[SIGN_UP_CODE_KEY] = value
        }

    override var accessToken: String
        get() = settings[ACCESS_TOKEN_KEY, ""]
        set(value) {
            settings[ACCESS_TOKEN_KEY] = value
        }

    private var accessTokenExpiresIn: Long
        get() = settings[ACCESS_TOKEN_EXPIRES_IN_KEY, 0L]
        set(value) {
            settings[ACCESS_TOKEN_EXPIRES_IN_KEY] = value
        }

    private var idToken: String
        get() = settings[ID_TOKEN_KEY, ""]
        set(value) {
            settings[ID_TOKEN_KEY] = value
        }

    override var refreshToken: String
        get() = settings[REFRESH_TOKEN_KEY, ""]
        set(value) {
            settings[REFRESH_TOKEN_KEY] = value
        }

    private var scope: String
        get() = settings[SCOPE_KEY, ""]
        set(value) {
            settings[SCOPE_KEY] = value
        }

    private var tenantCompliant: Boolean
        get() = settings[TENANT_COMPLIANT_KEY, false]
        set(value) {
            settings[TENANT_COMPLIANT_KEY] = value
        }

    private var tenantConsentCompliant: Boolean
        get() = settings[TENANT_CONSENT_COMPLIANT_KEY, false]
        set(value) {
            settings[TENANT_CONSENT_COMPLIANT_KEY] = value
        }

    override var tokenType: String
        get() = settings[TOKEN_TYPE_KEY, ""]
        set(value) {
            settings[TOKEN_TYPE_KEY] = value
        }

    override var salt: String
        get() = settings[SALT_KEY, ""]
        set(value) {
            settings[SALT_KEY] = value
        }

    override fun read(): VYouCredentials {
        when {
            accessToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read accessToken is empty")
            accessTokenExpiresIn == 0L -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read expiresIn is invalid")
            idToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read idToken is empty")
            refreshToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read refreshToken is empty")
            scope.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read scope is empty")
            tokenType.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::read tokenType is empty")
        }
        return VYouCredentials(accessToken, accessTokenExpiresIn, idToken, refreshToken, scope, tenantCompliant, tenantConsentCompliant, tokenType)
    }

    override fun save(credentials: VYouCredentials) {
        when {
            credentials.accessToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save accessToken is empty")
            credentials.expiresIn == 0L -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save expiresIn is invalid")
            credentials.idToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save idToken is empty")
            credentials.refreshToken.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save refreshToken is empty")
            credentials.scope.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save scope is empty")
            credentials.tokenType.isEmpty() -> throw VYouError(VYouErrorCode.CREDENTIALS, "VYouCredentials::save tokenType is empty")
        }

        accessToken = credentials.accessToken
        accessTokenExpiresIn = Clock.System.now().epochSeconds + credentials.expiresIn
        idToken = credentials.idToken
        refreshToken = credentials.refreshToken
        scope = credentials.scope
        tenantCompliant = credentials.tenantCompliant
        tenantConsentCompliant = credentials.tenantConsentCompliant
        tokenType = credentials.tokenType
    }

    override fun clearTokens() {
        settings.remove(ACCESS_TOKEN_KEY)
        settings.remove(REFRESH_TOKEN_KEY)
        settings.remove(ID_TOKEN_KEY)
    }

    override fun clear() = settings.clear()

    override fun isValid(): Boolean = accessTokenExpiresIn >= Clock.System.now().epochSeconds

    override fun isLogged(): Boolean = accessToken.isNotEmpty()

    companion object {
        const val NAME = "com.vyou.kmm.client.credentials:SETTINGS"
        const val CLIENT_ID_KEY = "com.vyou.kmm.client.credentials:CLIENT_ID_KEY"
        const val EMAIL_KEY = "com.vyou.kmm.client.credentials:EMAIL_KEY"
        const val SIGN_UP_CODE_KEY = "com.vyou.kmm.client.credentials:SIGN_UP_CODE_KEY"
        const val ACCESS_TOKEN_KEY = "com.vyou.kmm.client.credentials:ACCESS_TOKEN_KEY"
        const val ACCESS_TOKEN_EXPIRES_IN_KEY = "com.vyou.kmm.client.credentials:ACCESS_TOKEN_EXPIRES_IN_KEY"
        const val ID_TOKEN_KEY = "com.vyou.kmm.client.credentials:ID_TOKEN_KEY"
        const val REFRESH_TOKEN_KEY = "com.vyou.kmm.client.credentials:REFRESH_TOKEN_KEY"
        const val SCOPE_KEY = "com.vyou.kmm.client.credentials:SCOPE_KEY"
        const val TENANT_COMPLIANT_KEY = "com.vyou.kmm.client.credentials:TENANT_COMPLIANT_KEY"
        const val TENANT_CONSENT_COMPLIANT_KEY = "com.vyou.kmm.client.credentials:TENANT_CONSENT_COMPLIANT_KEY"
        const val TOKEN_TYPE_KEY = "com.vyou.kmm.client.credentials:TOKEN_TYPE_KEY"
        const val SALT_KEY = "com.vyou.kmm.client.credentials:SALT_KEY"
    }
}
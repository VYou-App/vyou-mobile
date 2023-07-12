package com.vyou.kmm.client.credentials

import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import kotlin.test.*

class CredentialsStorageRepositoryTest {
    private val mockSettings = SettingsMock()
    private val sut = CredentialsStorageRepository(mockSettings)

    @Test
    fun saveCredentialsThrowsErrorWhenAccessTokenIsEmpty() {
        assertSaveCredentialsFails()
    }

    @Test
    fun saveCredentialsThrowsErrorWhenExpiresInIsZero() {
        assertSaveCredentialsFails(accessToken = "accessToken")
    }

    @Test
    fun saveCredentialsThrowsErrorWhenIdTokenIsEmpty() {
        assertSaveCredentialsFails(accessToken = "accessToken", expiresIn = 100L)
    }

    @Test
    fun saveCredentialsThrowsErrorWhenRefreshTokenIsEmpty() {
        assertSaveCredentialsFails(accessToken = "accessToken", expiresIn = 100L, idToken = "idToken")
    }

    @Test
    fun saveCredentialsThrowsErrorWhenScopeIsEmpty() {
        assertSaveCredentialsFails(accessToken = "accessToken", expiresIn = 100L, idToken = "idToken", refreshToken = "refreshToken")
    }

    @Test
    fun saveCredentialsThrowsErrorWhenTokenTypeIsEmpty() {
        assertSaveCredentialsFails(accessToken = "accessToken", expiresIn = 100L, idToken = "idToken", refreshToken = "refreshToken", scope = "scope")
    }

    @Test
    fun saveCredentialsWorksCorrectlyWhenAllParametersAreValid() {
        val credentials = dummyCredentials(accessToken = "accessToken", expiresIn = 100L, idToken = "idToken", refreshToken = "refreshToken", scope = "scope", tokenType = "tokenType")
        sut.save(credentials)
    }

    @Test
    fun readCredentialsThrowsErrorWhenAnyStringIsEmpty() {
        assertReadCredentialsFails()
    }

    @Test
    fun readCredentialsThrowsErrorWhenAnyLongIsZero() {
        assertReadCredentialsFails()
    }

    @Test
    fun readCredentialsSuccessWhenAllPropertiesAreValid() {
        mockSettings.stringValue = "string"
        mockSettings.longValue = 1000L

        val credentials = sut.read()
        assertNotNull(credentials)
    }

    private fun assertSaveCredentialsFails(accessToken: String = "", expiresIn: Long = 0L, idToken: String = "", refreshToken: String = "", scope: String = "", tenantCompliant: Boolean = false, tenantConsentCompliant: Boolean = false, tokenType: String = "") {
        val actual = assertFailsWith<VYouError> {
            val credentials = dummyCredentials(accessToken, expiresIn, idToken, refreshToken, scope, tenantCompliant, tenantConsentCompliant, tokenType)
            sut.save(credentials)
        }
        assertEquals(VYouErrorCode.CREDENTIALS, actual.code)
    }

    private fun assertReadCredentialsFails() {
        val actual = assertFailsWith<VYouError> {
            sut.read()
        }
        assertEquals(VYouErrorCode.CREDENTIALS, actual.code)
    }

    private fun dummyCredentials(accessToken: String = "", expiresIn: Long = 0L, idToken: String = "", refreshToken: String = "", scope: String = "", tenantCompliant: Boolean = false, tenantConsentCompliant: Boolean = false, tokenType: String = "") =
        VYouCredentials(accessToken, expiresIn, idToken, refreshToken, scope, tenantCompliant, tenantConsentCompliant, tokenType)
}
package com.vyou.kmm.client.credentials

import com.vyou.kmm.client.VYouCredentials

interface CredentialsRepository {
    var clientId: String

    var email: String
    var signUpCode: String

    var accessToken: String
    var refreshToken: String
    var tokenType: String
    var salt: String

    fun save(credentials: VYouCredentials)
    fun clear()
    fun clearTokens()
    fun read(): VYouCredentials
    fun isValid(): Boolean
    fun isLogged(): Boolean
}
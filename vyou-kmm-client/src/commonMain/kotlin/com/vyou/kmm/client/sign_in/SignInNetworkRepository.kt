package com.vyou.kmm.client.sign_in

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.VYouSignInProvider
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.common.data.PKCE
import com.vyou.kmm.client.common.domain.Validation
import com.vyou.kmm.client.common.domain.ValidationParamRules
import com.vyou.kmm.client.common.domain.ValidationRule
import com.vyou.kmm.client.credentials.CredentialsRepository

internal class SignInNetworkRepository(private val client: NetworkClient, private val config: VYouConfig, private val credentialsRepository: CredentialsRepository): SignInRepository {
    override suspend fun signIn(params: VYouSignInProvider.UserPassword, pkce: PKCE): VYouCredentials {
        val paramRulesList = listOf(
            ValidationParamRules(params.username, listOf(ValidationRule.REQUIRED, ValidationRule.EMAIL)),
            ValidationParamRules(params.password, listOf(ValidationRule.REQUIRED))
        )
        Validation.applyRules(paramRulesList)

        val authDTO = AuthDTO(
            codeChallenge = pkce.challenge,
            clientId = config.clientId,
            username = params.username,
            password = params.password
        )

        val authorize = client.post<AuthorizeDTO>(Endpoint.Auth.path, authDTO)

        val loginDTO = LoginDTO(
            code = authorize.code,
            codeVerifier = pkce.verifier,
            clientId = config.clientId,
        )

        val result = client.post<CredentialsDTO>(Endpoint.Login.path, loginDTO)
        val credentials = result.toDomain()
        credentialsRepository.save(credentials)
        return credentials
    }
}
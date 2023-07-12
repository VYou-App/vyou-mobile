package com.vyou.kmm.client.sign_in_social

import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.common.data.ClientCredentialsManager
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.common.domain.Validation
import com.vyou.kmm.client.common.domain.ValidationParamRules
import com.vyou.kmm.client.common.domain.ValidationRule
import com.vyou.kmm.client.credentials.CredentialsRepository
import com.vyou.kmm.client.sign_in.CredentialsDTO

internal class SignInSocialNetworkRepository(private val client: NetworkClient, private val credentialsRepository: CredentialsRepository, private val clientCredentialsManager: ClientCredentialsManager): SignInSocialRepository {
    override suspend fun signIn(social: SignInSocial): VYouCredentials {
        val paramRulesList = listOf(
            ValidationParamRules(social.accessToken, listOf(ValidationRule.REQUIRED))
        )
        Validation.applyRules(paramRulesList)

        val result = client.post<CredentialsDTO>(
            endpoint = Endpoint.Social(social).path,
            dto = SignInSocialDTO(social.accessToken),
            Pair(HEADER_CLIENT_CREDENTIALS, clientCredentialsManager.getClientCredentials())
        )
        val credentials = result.toDomain()
        credentialsRepository.save(credentials)
        return credentials
    }

    private companion object {
        const val HEADER_CLIENT_CREDENTIALS = "Client-Credentials"
    }
}
package com.vyou.kmm.client.refresh_token

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.credentials.CredentialsRepository
import com.vyou.kmm.client.sign_in.CredentialsDTO

internal class RefreshTokenNetworkRepository(private val client: NetworkClient, private val credentialsRepository: CredentialsRepository, private val config: VYouConfig): RefreshTokenRepository {
    override suspend fun refreshToken(): VYouCredentials {
        val refreshToken = credentialsRepository.refreshToken
        val clientId = config.clientId
        val result = client.post<CredentialsDTO>(Endpoint.RefreshToken.path, RefreshTokenDTO(refreshToken, clientId))
        val credentials = result.toDomain()
        credentialsRepository.save(credentials)
        return credentials
    }
}
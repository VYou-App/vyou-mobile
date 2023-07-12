package com.vyou.kmm.client.backend

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.credentials.CredentialsRepository

class BackendNetworkRepository(private val client: NetworkClient, private val repository: CredentialsRepository, private val config: VYouConfig) : BackendRepository {
    override suspend fun updateSalt() {
        if(repository.clientId != config.clientId || repository.salt.isEmpty()) {
            runCatching {
                client.get<BackendDTO>(Endpoint.Backend.path)
            }.onFailure {
                throw VYouError(VYouErrorCode.SALT, it.message)
            }.onSuccess { dto ->
                repository.salt = dto.salt
            }
        }
    }
}
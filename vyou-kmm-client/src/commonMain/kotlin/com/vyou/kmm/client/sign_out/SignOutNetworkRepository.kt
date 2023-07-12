package com.vyou.kmm.client.sign_out

import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.credentials.CredentialsRepository

internal class SignOutNetworkRepository(private val client: NetworkClient, private val credentialsRepository: CredentialsRepository): SignOutRepository {
    override suspend fun logOut() {
        credentialsRepository.clear()
        client.postU(Endpoint.Logout.path)
    }
}
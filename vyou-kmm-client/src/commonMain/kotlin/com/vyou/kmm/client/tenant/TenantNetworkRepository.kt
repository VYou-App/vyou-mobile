package com.vyou.kmm.client.tenant

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient

internal class TenantNetworkRepository(private val client: NetworkClient, private val config: VYouConfig): TenantRepository {
    override suspend fun getTenant(): TenantDTO = client.get(Endpoint.Tenant(config.clientId).path)
}
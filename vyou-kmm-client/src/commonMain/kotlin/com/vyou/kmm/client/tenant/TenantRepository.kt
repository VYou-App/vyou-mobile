package com.vyou.kmm.client.tenant

internal interface TenantRepository {
    suspend fun getTenant(): TenantDTO
}
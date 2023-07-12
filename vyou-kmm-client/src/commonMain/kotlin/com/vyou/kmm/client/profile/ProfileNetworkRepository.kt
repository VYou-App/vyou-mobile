package com.vyou.kmm.client.profile

import com.vyou.kmm.client.VYouEditProfileParams
import com.vyou.kmm.client.VYouProfile
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.tenant.TenantDTO
import com.vyou.kmm.client.tenant.TenantRepository

internal class ProfileNetworkRepository(private val client: NetworkClient, private val tenantRepository: TenantRepository): ProfileRepository {
    private var tenant: TenantDTO? = null
    override suspend fun profile(): VYouProfile {
        if(tenant == null) {
            tenant = runCatching { tenantRepository.getTenant() }.getOrNull()
        }
        val profile = client.get<ProfileDTO>(Endpoint.Profile.path)
        return profile.toDomain(tenant)
    }


    override suspend fun editProfile(params: VYouEditProfileParams): VYouProfile {
        if(tenant == null) {
            tenant = runCatching { tenantRepository.getTenant() }.getOrNull()
        }
        val profile = client.put<ProfileDTO>(Endpoint.Profile.path, ProfileEditDTO.from(params))
        return profile.toDomain(tenant)
    }
}
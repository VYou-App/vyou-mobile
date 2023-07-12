package com.vyou.kmm.client.tenant

import com.vyou.kmm.client.common.data.FieldDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TenantDTO(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("allowed_roles")
    val allowedRoles: List<String>,
    @SerialName("fields")
    val fields: List<FieldDTO>
)

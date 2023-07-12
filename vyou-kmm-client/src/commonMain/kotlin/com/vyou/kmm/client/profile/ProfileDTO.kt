package com.vyou.kmm.client.profile

import com.vyou.kmm.client.*
import com.vyou.kmm.client.common.data.FieldTypeDTO
import com.vyou.kmm.client.tenant.TenantDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileDTO(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("fields")
    val fields: Map<String, String>,
    @SerialName("tenant_compliant")
    val tenantCompliant: Boolean,
    @SerialName("tenant_roles")
    val tenantRoles: List<String> = emptyList()
) {
    fun toDomain(tenant: TenantDTO?): VYouProfile = VYouProfile(
        id = id,
        email = email,
        fields = mapFields(tenant),
        tenantCompliant = tenantCompliant,
        tenantRoles = tenantRoles
    )

    private fun mapFields(tenant: TenantDTO?): List<VYouField> {
        return tenant?.let { tenantDto ->
            tenantDto.fields.sortedBy { it.order }.map { field ->
                when (field.type) {
                    FieldTypeDTO.number -> VYouFieldNumber(fields[field.name]?.toDouble(), field.name, field.required, field.readOnly)
                    FieldTypeDTO.string -> VYouFieldText(fields[field.name], field.name, field.required, field.readOnly)
                    FieldTypeDTO.email -> VYouFieldEmail(fields[field.name], field.name, field.required, field.readOnly)
                    FieldTypeDTO.date -> VYouFieldDate(fields[field.name]?.toLong(), field.name, field.required, field.readOnly)
                }
            }
        } ?: run {
            fields.map { (key, value) ->
                when {
                    value.toDoubleOrNull() != null -> VYouFieldNumber(value.toDouble(), key)
                    value.toLongOrNull() != null -> VYouFieldDate(value.toLong(), key)
                    else -> VYouFieldText(value, key)
                }
            }
        }
    }
}

@Serializable
data class ProfileEditDTO(
    val fields: Map<String, String?>
) {
    companion object {
        fun from(params: VYouEditProfileParams) = ProfileEditDTO(
            fields = mapFields(params.fields)
        )

        private fun mapFields(list: List<VYouField>): Map<String, String?> {
            val map = mutableMapOf<String, String?>()
            list.forEach { field ->
                when (field) {
                    is VYouFieldText -> map[field.name] = field.value
                    is VYouFieldNumber -> map[field.name] = field.value.toString()
                    is VYouFieldEmail -> map[field.name] = field.value
                    is VYouFieldDate -> map[field.name] = field.value.toString()
                }
            }
            return map.toMap()
        }
    }
}
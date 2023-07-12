package com.vyou.kmm.client.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FieldDTO(
    @SerialName("name")
    val name: String,
    @SerialName("required")
    val required: Boolean,
    @SerialName("readonly")
    val readOnly: Boolean,
    @SerialName("type")
    val type: FieldTypeDTO,
    @SerialName("order")
    val order: Int,
)

internal enum class FieldTypeDTO {
    number, string, email, date
}

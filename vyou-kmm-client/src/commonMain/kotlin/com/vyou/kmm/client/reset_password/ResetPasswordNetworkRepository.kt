package com.vyou.kmm.client.reset_password

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouResetPasswordParams
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.common.domain.Validation
import com.vyou.kmm.client.common.domain.ValidationParamRules
import com.vyou.kmm.client.common.domain.ValidationRule

internal class ResetPasswordNetworkRepository(private val client: NetworkClient, private val config: VYouConfig): ResetPasswordRepository {
    override suspend fun reset(params: VYouResetPasswordParams) {
        val paramRulesList = listOf(
            ValidationParamRules(params.email, listOf(ValidationRule.REQUIRED, ValidationRule.EMAIL)),
        )
        Validation.applyRules(paramRulesList)

        client.postU(Endpoint.ResetPassword.path, ResetPasswordDTO.from(params, config.clientId))
    }
}
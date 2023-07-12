package com.vyou.kmm.client.sign_up

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouSignUpParams
import com.vyou.kmm.client.VYouSignUpVerifyParams
import com.vyou.kmm.client.common.data.Endpoint
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.common.domain.Validation
import com.vyou.kmm.client.common.domain.ValidationParamRules
import com.vyou.kmm.client.common.domain.ValidationRule
import com.vyou.kmm.client.credentials.CredentialsRepository

internal class SignUpNetworkRepository(private val client: NetworkClient, private val storageRepository: CredentialsRepository, private val config: VYouConfig): SignUpRepository {
    override suspend fun register(params: VYouSignUpParams) {
        val paramRulesList = listOf(
            ValidationParamRules(params.username, listOf(ValidationRule.REQUIRED, ValidationRule.EMAIL)),
            ValidationParamRules(params.termsConditions, listOf(ValidationRule.REQUIRED, ValidationRule.TRUE)),
            ValidationParamRules(params.privacyPolicy, listOf(ValidationRule.REQUIRED, ValidationRule.TRUE))
        )
        Validation.applyRules(paramRulesList)

        client.postU(Endpoint.Profile.path, RegisterDTO.from(params, config.clientId))
    }

    override suspend fun verify(params: VYouSignUpVerifyParams) {
        val paramRulesList = listOf(
            ValidationParamRules(params.code, listOf(ValidationRule.REQUIRED)),
        )
        Validation.applyRules(paramRulesList)
        val dto = client.get<VerifyDTO>(Endpoint.Verify(params.code).path)

        storageRepository.signUpCode = params.code
        storageRepository.email = dto.email
    }

    override suspend fun passwords(encryptedPassword: String) {
        val code = storageRepository.signUpCode
        val paramRulesList = listOf(
            ValidationParamRules(encryptedPassword, listOf(ValidationRule.REQUIRED, ValidationRule.ENCRYPTED_PASSWORD)),
            ValidationParamRules(code, listOf(ValidationRule.REQUIRED))
        )
        Validation.applyRules(paramRulesList)

        client.postU(Endpoint.RegisterPassword.path, RegisterPasswordsDTO(encryptedPassword, code))
    }
}
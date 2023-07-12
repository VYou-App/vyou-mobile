package com.vyou.kmm.client.common.domain

import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValidationTest {

    @Test
    fun applyRulesSuccessWhenRulesAreEmpty() {
        Validation.applyRules(listOf())
    }

    @Test
    fun applyRulesThrowsErrorWhenAnyParameterIsEmptyWithRequiredRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("", listOf(ValidationRule.REQUIRED))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidEmailWithIsEmailRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("email", listOf(ValidationRule.EMAIL))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidEmailWithIsEmailRule() {
        Validation.applyRules(listOf(ValidationParamRules("email@email.com", listOf(ValidationRule.EMAIL))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidNumberWithIsNumberRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("number", listOf(ValidationRule.NUMBER))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidNumberWithIsNumberRule() {
        Validation.applyRules(listOf(ValidationParamRules(0, listOf(ValidationRule.NUMBER))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidBoolWithIsTrueRule() {
        assertApplyRulesFails(listOf(ValidationParamRules(false, listOf(ValidationRule.TRUE))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidBoolWithIsNumberRule() {
        Validation.applyRules(listOf(ValidationParamRules(true, listOf(ValidationRule.TRUE))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidWithIsLongEnoughPasswordRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("12345", listOf(ValidationRule.LONG_ENOUGH_PASSWORD))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidBoolWithIsLongEnoughPasswordRule() {
        Validation.applyRules(listOf(ValidationParamRules("123456", listOf(ValidationRule.LONG_ENOUGH_PASSWORD))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidWithIsFairEnoughPasswordRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("123456", listOf(ValidationRule.FAIR_ENOUGH_PASSWORD))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidBoolWithIsFairEnoughPasswordRule() {
        Validation.applyRules(listOf(ValidationParamRules("123abc", listOf(ValidationRule.FAIR_ENOUGH_PASSWORD))))
    }

    @Test
    fun applyRulesThrowsErrorWhenParameterIsNotValidWithIsEncryptedPasswordRule() {
        assertApplyRulesFails(listOf(ValidationParamRules("password", listOf(ValidationRule.ENCRYPTED_PASSWORD))))
    }

    @Test
    fun applyRulesSuccessWhenParameterIsValidBoolWithIsEncryptedPasswordRule() {
        Validation.applyRules(listOf(ValidationParamRules("\$2b\$12\$x1jJ7V5wGziMqbNOgnbNfu6k5ZqQiabyhGSMWKSFCnb7EQwPeeJ8a", listOf(ValidationRule.ENCRYPTED_PASSWORD))))
    }

//    @Test
//    fun applyRulesThrowsErrorWhenParameterIsNotValidDateWithIsDateRule() {
//        assertApplyRulesFails(listOf(ValidationParamRules("date", listOf(ValidationRule.isDate))))
//    }
//
//    @Test
//    fun applyRulesSuccessWhenParameterIsValidDateWithIsDateRule() {
//        Validation.applyRules(listOf(ValidationParamRules("2022-01-25 09:00:00", listOf(ValidationRule.isDate))))
//    }

    private fun assertApplyRulesFails(list: List<ValidationParamRules>) {
        val actual = assertFailsWith<VYouError> {
            Validation.applyRules(list)
        }
        val expected = VYouErrorCode.VALIDATION_PARAMS
        assertEquals(expected, actual.code)
    }


}
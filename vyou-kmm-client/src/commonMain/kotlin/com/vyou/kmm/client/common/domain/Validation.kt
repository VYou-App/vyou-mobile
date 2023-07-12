package com.vyou.kmm.client.common.domain

import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode

object Validation {

    fun applyRules(list: List<ValidationParamRules>) {
        val keyValidation: MutableList<ValidationParamRules> = mutableListOf()

        list.forEach { paramRules ->
            val invalidRules = invalidRules(paramRules)
            if(invalidRules.isNotEmpty()) {
                keyValidation.add(ValidationParamRules(paramRules.param, invalidRules))
            }
        }

        if(keyValidation.isNotEmpty()) {
            throw VYouError(VYouErrorCode.VALIDATION_PARAMS, "Validation error: ${keyValidation.toList()}")
        }
    }

    private fun invalidRules(validationParamRules: ValidationParamRules): List<ValidationRule> {
        val validation = mutableListOf<ValidationRule>()
        val param = validationParamRules.param
        validationParamRules.rules.forEach { validationType ->
            when (validationType) {
                ValidationRule.REQUIRED -> if(validateRequired(param).not()) validation.add(validationType)
                ValidationRule.NUMBER -> if(validateNumber(param).not()) validation.add(validationType)
                ValidationRule.EMAIL -> if(validateEmail(param).not()) validation.add(validationType)
                ValidationRule.DATE -> if(validateDate(param).not()) validation.add(validationType)
                ValidationRule.TRUE -> if(validateTrue(param).not()) validation.add(validationType)
                ValidationRule.LONG_ENOUGH_PASSWORD -> if(validatePasswordStrength(param) == 0) validation.add(validationType)
                ValidationRule.FAIR_ENOUGH_PASSWORD -> if(validatePasswordStrength(param) < 2) validation.add(validationType)
                ValidationRule.ENCRYPTED_PASSWORD -> if(validatePasswordEncrypt(param).not()) validation.add(validationType)
            }
        }
        return validation.toList()
    }

    private fun validateRequired(input: Any?) = (input is String && input.isNotBlank()) || (input !is String && input != null)

    private fun validateNumber(input: Any?) = input is Int || input is Long || (input is String && input.toLongOrNull() != null)

    private fun validateString(input: Any?) = input is String

    private fun validateEmail(input: Any?) = validateString(input) && Regex(PATTERN_EMAIL).matches(input as String)

    //TODO: Check input date java
    private fun validateDate(input: Any?) = (input is String && Regex(PATTERN_DATE).matches(input))

    private fun validateTrue(input: Any?) = input is Boolean && input

    private fun validatePasswordStrength(input: Any?): Int {
        if(input !is String || input.length < 6) {
            return 0
        }
        var result = 0

        result += if (Regex(PATTERN_UPPERCASE).containsMatchIn(input)) 1 else 0
        result += if (Regex(PATTERN_LOWERCASE).containsMatchIn(input)) 1 else 0
        result += if (Regex(PATTERN_DIGITS).containsMatchIn(input)) 1 else 0
        result += if (Regex(PATTERN_SPECIAL).containsMatchIn(input)) 1 else 0

        return result
    }

    private fun validatePasswordEncrypt(input: Any?) = input is String && input.startsWith("\$2b\$12\$") && input.length == ENCRYPTED_PASSWORD_LENGTH

    private const val PATTERN_EMAIL = "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"

    private const val PATTERN_DATE = "[0-9]{4}-[0-9]{2}-[0-9]{2}"

    private const val PATTERN_UPPERCASE = "[A-Z]"
    private const val PATTERN_LOWERCASE = "[a-z]"
    private const val PATTERN_DIGITS = "[0-9]"
    private const val PATTERN_SPECIAL = "[^A-Za-z0-9]"
}

data class ValidationParamRules(
    val param: Any?,
    val rules: List<ValidationRule>
)

enum class ValidationRule {
    REQUIRED,
    NUMBER,
    EMAIL,
    DATE,
    TRUE,
    LONG_ENOUGH_PASSWORD,
    FAIR_ENOUGH_PASSWORD,
    ENCRYPTED_PASSWORD
}

private const val ENCRYPTED_PASSWORD_LENGTH = 60
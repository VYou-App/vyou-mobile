package com.vyou.kmm.client.common.data

import android.util.Base64
import at.favre.lib.crypto.bcrypt.BCrypt
import com.vyou.kmm.client.common.domain.Validation
import com.vyou.kmm.client.common.domain.ValidationParamRules
import com.vyou.kmm.client.common.domain.ValidationRule
import java.security.MessageDigest
import java.security.SecureRandom

internal class Crypt(private val publicSaltBase64: String) {
    fun generatePKCE(): PKCE {
        val sha1Random = SecureRandom.getInstance(ALGO_SHA_1)
        sha1Random.setSeed(sha1Random.generateSeed(8))
        val values = ByteArray(32)
        sha1Random.nextBytes(values)
        val verifier = Base64.encodeToString(values, Base64.URL_SAFE + Base64.NO_PADDING + Base64.NO_WRAP)

        val bytes = verifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance(ALGO_SHA_256)
        md.update(bytes, 0, bytes.size)
        val digest = md.digest()
        val challenge = Base64.encodeToString(digest, Base64.URL_SAFE + Base64.NO_PADDING + Base64.NO_WRAP)
        return PKCE(verifier, challenge)
    }

    fun encryptPassword(email: String, password: String): String {
        Validation.applyRules(
            listOf(
                ValidationParamRules(email, listOf(ValidationRule.REQUIRED, ValidationRule.EMAIL)),
                ValidationParamRules(password, listOf(ValidationRule.REQUIRED, ValidationRule.FAIR_ENOUGH_PASSWORD))
            )
        )

        val chain = publicSaltBase64 + email
        val sha512 = MessageDigest.getInstance(ALGO_SHA_512)
        val saltBytes = chain.toByteArray()
        sha512.reset()
        sha512.update(saltBytes)
        val bytes = sha512.digest().take(16).toByteArray()
        val encryptedPasswordByteArray = BCrypt.with(BCrypt.Version.VERSION_2B).hash(12, bytes, password.toByteArray())
        return encryptedPasswordByteArray.decodeToString()
    }

    private companion object {
        const val ALGO_SHA_1 = "SHA1PRNG"
        const val ALGO_SHA_256 = "SHA-256"
        const val ALGO_SHA_512 = "SHA-512"
    }
}
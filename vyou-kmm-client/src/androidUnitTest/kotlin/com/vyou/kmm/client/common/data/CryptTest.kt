package com.vyou.kmm.client.common.data

import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class CryptTest {
    private val sut = Crypt("SJZgbruvbTF5k0sNNB3lnQ")

    @Test
    fun `encrypt password throws error due to empty email`() {
        assertEncryptPasswordFails("", "password")
    }

    @Test
    fun `encrypt password throws error due to empty password`() {
        assertEncryptPasswordFails("test@test.com", "")
    }

    @Test
    fun `encrypt password throws error due to invalid email`() {
        assertEncryptPasswordFails("test@test", "password")
    }

    @Test
    fun `encrypt password throws error due to password only letters`() {
        assertEncryptPasswordFails("test@test.com", "password")
    }

    @Test
    fun `encrypt password throws error due to password only numbers`() {
        assertEncryptPasswordFails("test@test.com", "12341234")
    }

    @Test
    fun `encrypt password throws error due to password only special characters`() {
        assertEncryptPasswordFails("test@test.com", "_!#@$%@#%")
    }

    @Test
    fun `encrypt password throws error due to password less than six characters`() {
        assertEncryptPasswordFails("test@test.com", "abc1@")
    }

    @Test
    fun `encrypt password returns expected value`() {
        val actual = sut.encryptPassword("test@test.com", "test1234")
        val expected = "\$2b\$12\$x1jJ7V5wGziMqbNOgnbNfu6k5ZqQiabyhGSMWKSFCnb7EQwPeeJ8a"
        assertEquals(expected, actual)
    }

    @Test
    fun `generate encrypt password`() {
        val actual = sut.encryptPassword("rubenef92@gmail.com", "test1234")
        print("Encrypted password: $actual")
        assertNotNull(actual)
    }

    private fun assertEncryptPasswordFails(email: String, password: String) {
        val actual = assertFailsWith(VYouError::class) {
            sut.encryptPassword(email, password)
        }
        val expected = VYouErrorCode.VALIDATION_PARAMS
        assertEquals(expected, actual.code)
    }
}
package com.vyou.kmm.client.common.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class CryptTestIT {
    private val sut = Crypt("SJZgbruvbTF5k0sNNB3lnQ")

    @Test
    fun generatePKCEWorksCorrectly() {
        val actual = sut.generatePKCE()
        println("Code challenge: ${actual.challenge}")
        println("Code verifier: ${actual.verifier}")
        assertNotNull(actual)
    }
}
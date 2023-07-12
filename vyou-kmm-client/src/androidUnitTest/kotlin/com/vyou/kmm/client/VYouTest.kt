package com.vyou.kmm.client

import org.junit.Assert.*
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class VYouTest {

    @Test
    fun `initialize client throws error when client id is empty`() {
        assertsFails(VYouErrorCode.INITIALIZE) {
            buildVYou(clientId = "").initialize()
        }
    }

    @Test
    fun `initialize client throws error when client id is not valid`() {
        assertsFails(VYouErrorCode.INITIALIZE) {
            buildVYou(clientId = "clientId").initialize()
        }
    }

    @Test
    fun `initialize client throws error when server url is empty`() {
        assertsFails(VYouErrorCode.INITIALIZE) {
            buildVYou(serverUrl = "").initialize()
        }
    }

    @Test
    fun `vYou instance throws error when is not initialized previously`() {
        assertsFails(VYouErrorCode.INSTANCE) {
            VYou.instance()
        }
    }
    
    private fun buildVYou(clientId: String = "QbqZjaZampkiuCfsaBxE4apuS4v0mJbr2Ka6U20aknRiPI9yYNH5dwJ2DA", serverUrl: String = "https://test.vyou-app.com:6120", appDeclaration: KoinAppDeclaration = {}): VYou.Builder {
        return VYou.Builder(clientId, serverUrl, appDeclaration)
    }

    private fun assertsFails(expected: VYouErrorCode, block: () -> Unit) {
        val actual = assertFailsWith<VYouError> {
            block()
        }
        assertEquals(expected, actual.code)
    }
}
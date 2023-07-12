package com.vyou.kmm.client

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import kotlin.test.assertNotNull

class VYouTestIT: KoinTest {

    @Test
    fun initializeClientCreatesInstanceWithValidParameters() {
        VYou.Builder(
            clientId = "QbqZjaZampkiuCfsaBxE4apuS4v0mJbr2Ka6U20aknRiPI9yYNH5dwJ2DA",
            serverUrl = "https://test.vyou-app.com:6120") {
            androidContext(InstrumentationRegistry.getInstrumentation().context)
        }.initialize()

        assertNotNull(VYou.instance())
    }
}
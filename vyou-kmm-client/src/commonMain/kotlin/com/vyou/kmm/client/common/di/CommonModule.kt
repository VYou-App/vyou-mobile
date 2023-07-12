package com.vyou.kmm.client.common.di

import com.vyou.kmm.client.VYouConfig
import org.koin.dsl.module

internal fun commonModule(serverUrl: String, clientId: String) = module {
    single { VYouConfig(serverUrl, clientId) }
}
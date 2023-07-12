package com.vyou.kmm.client.common.di

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.credentials.CredentialsStorageRepository
import io.ktor.client.engine.darwin.*
import org.koin.dsl.module

@ExperimentalSettingsImplementation
actual fun platformModule() = module {
    single { Darwin.create() }

    single<Settings> {
        val config = get<VYouConfig>()
        KeychainSettings(service = "${CredentialsStorageRepository.NAME}-${config.clientId}",)
    }
}
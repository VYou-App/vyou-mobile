package com.vyou.kmm.client.common.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.credentials.CredentialsStorageRepository
import io.ktor.client.engine.android.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Android.create() }

    single<Settings> {
        val config = get<VYouConfig>()
        SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                androidContext(),
                "${CredentialsStorageRepository.NAME}-${config.clientId}",
                MasterKey.Builder(androidContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        )
    }
}
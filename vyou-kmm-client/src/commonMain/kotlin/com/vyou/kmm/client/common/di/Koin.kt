package com.vyou.kmm.client.common.di

import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouLogLevel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

internal fun initKoin(serverUrl: String, clientId: String, modules: List<Module>, networkLogLevel: VYouLogLevel, onRefreshTokenFailure: (VYouError) -> Unit, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        loadKoinModules(
            listOf(
                commonModule(serverUrl, clientId),
                networkModule(networkLogLevel, onRefreshTokenFailure),
                platformModule(),
                featureModule
            ) + modules
        )
    }
}

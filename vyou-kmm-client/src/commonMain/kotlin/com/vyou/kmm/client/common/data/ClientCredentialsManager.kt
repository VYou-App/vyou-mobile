package com.vyou.kmm.client.common.data

import com.vyou.kmm.client.VYouConfig
import io.ktor.util.*

internal class ClientCredentialsManager(private val config: VYouConfig) {
    fun getClientCredentials(): String {
        return "Basic ${"${config.clientId}:${config.clientId}".encodeBase64()}"
    }
}

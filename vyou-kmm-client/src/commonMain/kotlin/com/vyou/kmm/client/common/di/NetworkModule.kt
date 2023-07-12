package com.vyou.kmm.client.common.di

import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import com.vyou.kmm.client.VYouLogLevel
import com.vyou.kmm.client.common.data.ClientCredentialsManager
import com.vyou.kmm.client.common.data.NetworkClient
import com.vyou.kmm.client.credentials.CredentialsRepository
import com.vyou.kmm.client.refresh_token.RefreshTokenRepository
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun networkModule(networkLogLevel: VYouLogLevel, onRefreshTokenFailure: (VYouError) -> Unit) = module {
    single(named(SERVER_BASIC_NAME)) {
        createHttpClient(get(), networkLogLevel, get())
    }

    single(named(SERVER_OAUTH_NAME)) {
        createHttpClient(get(), networkLogLevel, get()) {
            install(Auth) {
                bearer {
                    loadTokens {
                        val repository = get<CredentialsRepository>()
                        BearerTokens(repository.accessToken, repository.refreshToken)
                    }
                    refreshTokens {
                        var accessToken = oldTokens?.accessToken ?: ""
                        var refreshToken = oldTokens?.refreshToken ?: ""

                        runCatching {
                            get<RefreshTokenRepository>().refreshToken()
                        }.onFailure { error ->
                            get<CredentialsRepository>().clearTokens()
                            onRefreshTokenFailure.invoke(VYouError(VYouErrorCode.NETWORK_REFRESH_TOKEN_EXPIRED, error.message))
                        }.onSuccess { credentials ->
                            accessToken = credentials.accessToken
                            refreshToken = credentials.refreshToken
                        }
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
                }
            }
        }
    }

    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single(named(SERVER_BASIC_NAME)) { NetworkClient(get(named(SERVER_BASIC_NAME)), get()) }
    single(named(SERVER_OAUTH_NAME)) { NetworkClient(get(named(SERVER_OAUTH_NAME)), get()) }
    single { ClientCredentialsManager(get()) }
}

private fun createHttpClient(httpClientEngine: HttpClientEngine, networkLogLevel: VYouLogLevel, json: Json, block: HttpClientConfig<*>.() -> Unit = {}): HttpClient =
    HttpClient(httpClientEngine) {
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        install(ContentNegotiation) {
            json(json)
        }
        if (networkLogLevel != VYouLogLevel.NONE) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = networkLogLevel.level
            }
        }
        defaultRequest {
            header(HEADER_USER_AGENT, HEADER_USER_AGENT_VALUE)
        }
        block()
    }

private const val HEADER_USER_AGENT = "User-Agent"
private const val HEADER_USER_AGENT_VALUE = "mobile"
internal const val SERVER_BASIC_NAME = "vyou-server-basic"
internal const val SERVER_OAUTH_NAME = "vyou-server-oauth"

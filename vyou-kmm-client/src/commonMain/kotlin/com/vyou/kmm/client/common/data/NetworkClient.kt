package com.vyou.kmm.client.common.data

import com.vyou.kmm.client.VYouConfig
import com.vyou.kmm.client.VYouError
import com.vyou.kmm.client.VYouErrorCode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.isSuccess

class NetworkClient(val client: HttpClient, val config: VYouConfig) {

    suspend inline fun <reified T> get(endpoint: String, parameters: Map<String, String> = emptyMap()): T = call {
        createGet(endpoint, parameters).body()
    }

    suspend fun getU(endpoint: String, parameters: Map<String, String> = emptyMap()) = validate {
        createGet(endpoint, parameters)
    }

    suspend inline fun <reified T> post(endpoint: String, dto: Any? = null, vararg headers: Pair<String, Any?>): T = call {
        createPost(endpoint, dto, headers).body()
    }

    suspend fun postU(endpoint: String, dto: Any? = null) = validate {
        createPost(endpoint, dto)
    }

    suspend inline fun <reified T> put(endpoint: String, dto: Any? = null): T = call {
        createPut(endpoint, dto).body()
    }

    suspend fun createGet(endpoint: String, parameters: Map<String, String>): HttpResponse = client.get {
        var query = ""
        parameters.forEach { entry ->
            query += if(query.isEmpty()) {
                "?"
            } else {
                "&"
            }
            query += "${entry.key}=${entry.value}"
        }
        url("${config.serverUrl}/$endpoint$query")
    }

    suspend fun createPost(endpoint: String, dto: Any?, headers: Array<out Pair<String, Any?>> = emptyArray()): HttpResponse = client.post {
        url("${config.serverUrl}/$endpoint")
        addBody(dto)
        headers.forEach { header(it.first, it.second) }
    }

    suspend fun createPut(endpoint: String, dto: Any?): HttpResponse = client.put {
        url("${config.serverUrl}/$endpoint")
        addBody(dto)
    }

    private fun HttpRequestBuilder.addBody(dto: Any?) {
        dto?.let {
            headers["Content-Type"] = "application/json"
            setBody(it)
        }
    }

    suspend fun <T> call(func: suspend () -> T): T =
        runCatching { func.invoke() }.getOrElse { throw handleError(it) }

    private suspend fun validate(func: suspend () -> HttpResponse) {
        runCatching { func.invoke() }
            .onSuccess {
                if(it.status.isSuccess().not()) throw VYouError(VYouErrorCode.NETWORK_ERROR, it.status.description, it.status.value)
            }
            .onFailure {
                throw handleError(it)
            }
    }

    private fun handleError(throwable: Throwable): VYouError = when (throwable) {
        is ClientRequestException -> VYouError(VYouErrorCode.NETWORK_REQUEST_ERROR, throwable.message, throwable.response.status.value)
        is ServerResponseException -> VYouError(VYouErrorCode.NETWORK_SERVER_ERROR, throwable.message, throwable.response.status.value)
        else -> VYouError(VYouErrorCode.NETWORK_ERROR, throwable.message)
    }
}
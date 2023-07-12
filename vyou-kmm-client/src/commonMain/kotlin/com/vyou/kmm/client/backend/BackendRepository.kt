package com.vyou.kmm.client.backend

interface BackendRepository {
    suspend fun updateSalt()
}
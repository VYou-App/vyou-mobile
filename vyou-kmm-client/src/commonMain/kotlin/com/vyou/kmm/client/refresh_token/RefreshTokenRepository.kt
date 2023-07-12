package com.vyou.kmm.client.refresh_token

import com.vyou.kmm.client.VYouCredentials

internal interface RefreshTokenRepository {
    suspend fun refreshToken(): VYouCredentials
}
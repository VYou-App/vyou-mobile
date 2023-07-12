package com.vyou.kmm.client.reset_password

import com.vyou.kmm.client.VYouResetPasswordParams

internal interface ResetPasswordRepository {
    suspend fun reset(params: VYouResetPasswordParams)
}
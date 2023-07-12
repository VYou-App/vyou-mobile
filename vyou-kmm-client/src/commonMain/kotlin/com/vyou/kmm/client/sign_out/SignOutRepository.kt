package com.vyou.kmm.client.sign_out

internal interface SignOutRepository {
    suspend fun logOut()
}
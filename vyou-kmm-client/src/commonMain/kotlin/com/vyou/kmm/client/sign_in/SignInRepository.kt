package com.vyou.kmm.client.sign_in

import com.vyou.kmm.client.VYouCredentials
import com.vyou.kmm.client.VYouSignInProvider
import com.vyou.kmm.client.common.data.PKCE

internal interface SignInRepository {
    suspend fun signIn(params: VYouSignInProvider.UserPassword, pkce: PKCE): VYouCredentials
}
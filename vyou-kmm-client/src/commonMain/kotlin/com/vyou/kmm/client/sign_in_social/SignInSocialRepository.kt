package com.vyou.kmm.client.sign_in_social

import com.vyou.kmm.client.VYouCredentials

internal interface SignInSocialRepository {
    suspend fun signIn(social: SignInSocial): VYouCredentials
}
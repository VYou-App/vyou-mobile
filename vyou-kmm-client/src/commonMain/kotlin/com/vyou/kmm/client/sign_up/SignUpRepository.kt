package com.vyou.kmm.client.sign_up

import com.vyou.kmm.client.VYouSignUpParams
import com.vyou.kmm.client.VYouSignUpPasswordParams
import com.vyou.kmm.client.VYouSignUpVerifyParams

internal interface SignUpRepository {
    suspend fun register(params: VYouSignUpParams)
    suspend fun verify(params: VYouSignUpVerifyParams)
    suspend fun passwords(encryptedPassword: String)
}
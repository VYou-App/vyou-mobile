package com.vyou.kmm.client.profile

import com.vyou.kmm.client.VYouEditProfileParams
import com.vyou.kmm.client.VYouProfile

internal interface ProfileRepository {
    suspend fun profile(): VYouProfile
    suspend fun editProfile(params: VYouEditProfileParams): VYouProfile
}
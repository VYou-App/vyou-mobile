package com.vyou.kmm.client.common.data

import com.vyou.kmm.client.sign_in_social.SignInSocial

internal sealed class Endpoint(val path: String) {
    object Auth : Endpoint("mobile/auth")
    object Login : Endpoint("mobile/login")
    object Logout : Endpoint("logout")
    object Profile : Endpoint("api/v1/customer")
    object RefreshToken : Endpoint("refreshtoken")
    object RegisterPassword : Endpoint("api/v1/password/register")
    object ResetPassword : Endpoint("api/v1/password/reset")
    data class Social(val social: SignInSocial) : Endpoint(social.endpoint())
    data class Verify(val code: String) : Endpoint("api/v1/password/verifycode/$code")
    data class Tenant(val clientId: String) : Endpoint("api/v1/tenant/$clientId")
    object Backend : Endpoint("api/v1/backend")
}
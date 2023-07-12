package com.vyou.kmm.client.sign_in_social


internal sealed class SignInSocial(open val accessToken: String) {
    class Google(override val accessToken: String): SignInSocial(accessToken)
    class Facebook(override val accessToken: String): SignInSocial(accessToken)
    class Apple(override val accessToken: String): SignInSocial(accessToken)

    fun endpoint() = when(this) {
        is Google -> "googlelogin"
        is Facebook -> "facebooklogin"
        is Apple -> "applelogin"
    }
}

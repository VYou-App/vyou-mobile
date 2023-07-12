package com.vyou.kmm.client

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.vyou.kmm.client.common.data.PKCE
import com.vyou.kmm.client.common.di.initKoin
import com.vyou.kmm.client.credentials.CredentialsRepository
import com.vyou.kmm.client.profile.ProfileRepository
import com.vyou.kmm.client.refresh_token.RefreshTokenRepository
import com.vyou.kmm.client.reset_password.ResetPasswordRepository
import com.vyou.kmm.client.sign_in.SignInRepository
import com.vyou.kmm.client.sign_in_social.SignInSocial
import com.vyou.kmm.client.sign_in_social.SignInSocialRepository
import com.vyou.kmm.client.sign_out.SignOutRepository
import com.vyou.kmm.client.sign_up.SignUpRepository
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import kotlin.coroutines.cancellation.CancellationException

class VYouClient internal constructor(clientId: String, serverUrl: String, networkLogLevel: VYouLogLevel, onRefreshTokenFailure: (VYouError) -> Unit, private val onSignOut: () -> Unit, modules: List<Module> = emptyList(), appDeclaration: KoinAppDeclaration = {}) : KoinComponent {
    private val credentialsRepository: CredentialsRepository by inject()
    private val signInRepository: SignInRepository by inject()
    private val signInSocialRepository: SignInSocialRepository by inject()
    private val signUpRepository: SignUpRepository by inject()
    private val signOutRepository: SignOutRepository by inject()
    private val refreshTokenRepository: RefreshTokenRepository by inject()
    private val resetPasswordRepository: ResetPasswordRepository by inject()
    private val profileRepository: ProfileRepository by inject()

    constructor(clientId: String, serverUrl: String, networkLogLevel: VYouLogLevel, onRefreshTokenFailure: (VYouError) -> Unit, onSignOut: () -> Unit) :
            this(clientId, serverUrl, networkLogLevel, onRefreshTokenFailure, onSignOut, emptyList(), {})

    init {
        initKoin(serverUrl, clientId, modules, networkLogLevel, onRefreshTokenFailure, appDeclaration)
    }

    //Credentials
    fun isLoggedIn(): Boolean = credentialsRepository.isLogged()

    @Throws(VYouError::class)
    fun credentials(): VYouCredentials = credentialsRepository.read()
    fun isValidToken(): Boolean = credentialsRepository.isValid()
    fun getEmail(): String = credentialsRepository.email
    fun getTokenType(): String = credentialsRepository.tokenType
    fun getAccessToken(): String = credentialsRepository.accessToken

    //Login
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signIn(params: VYouSignInProvider.UserPassword, pkce: PKCE): VYouCredentials =
        signInRepository.signIn(params, pkce)

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signInGoogle(params: VYouSignInProvider.Google): VYouCredentials =
        signInSocialRepository.signIn(SignInSocial.Google(params.accessToken))

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signInFacebook(params: VYouSignInProvider.Facebook): VYouCredentials =
        signInSocialRepository.signIn(SignInSocial.Facebook(params.accessToken))

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signInApple(params: VYouSignInProvider.Apple): VYouCredentials =
        signInSocialRepository.signIn(SignInSocial.Apple(params.accessToken))

    //Register
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signUp(params: VYouSignUpParams) =
        signUpRepository.register(params)

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signUpVerify(params: VYouSignUpVerifyParams) =
        signUpRepository.verify(params)

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signUpPasswords(encryptedPassword: String) =
        signUpRepository.passwords(encryptedPassword)

    //Logout
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun signOut() =
        signOutRepository.logOut().also { onSignOut.invoke() }

    //Reset password
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun resetPassword(params: VYouResetPasswordParams) =
        resetPasswordRepository.reset(params)

    //Refresh token
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun refreshToken(): VYouCredentials =
        refreshTokenRepository.refreshToken()

    //Profile
    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun getProfile(): VYouProfile =
        profileRepository.profile()

    @NativeCoroutines
    @Throws(VYouError::class, CancellationException::class)
    suspend fun editProfile(params: VYouEditProfileParams): VYouProfile =
        profileRepository.editProfile(params)
}
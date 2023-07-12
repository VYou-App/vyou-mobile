package com.vyou.kmm.client

import com.vyou.kmm.client.common.data.Crypt
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

class VYou private constructor(private val client: VYouClient, private val crypt: Crypt) {

    // Credentials
    fun isLoggedIn(): Boolean = client.isLoggedIn()
    fun tokenType(): String = client.getTokenType()
    fun accessToken(): String = client.getAccessToken()
    fun credentials(): VYouCredentials = client.credentials()
    fun isValidToken(): Boolean = client.isValidToken()

    // Login
    suspend fun signIn(provider: VYouSignInProvider): VYouCredentials = when (provider) {
        is VYouSignInProvider.UserPassword -> client.signIn(provider.copy(password = crypt.encryptPassword(provider.username, provider.password)), crypt.generatePKCE())
        is VYouSignInProvider.Google -> client.signInGoogle(provider)
        is VYouSignInProvider.Facebook -> client.signInFacebook(provider)
        is VYouSignInProvider.Apple -> client.signInApple(provider)
    }

    // Register
    suspend fun signUp(params: VYouSignUpParams) = client.signUp(params)
    suspend fun signUpVerify(params: VYouSignUpVerifyParams) = client.signUpVerify(params)
    suspend fun signUpPasswords(params: VYouSignUpPasswordParams) = client.signUpPasswords(crypt.encryptPassword(client.getEmail(), params.password))

    // Logout
    suspend fun signOut() = client.signOut()

    // Reset Password
    suspend fun resetPassword(params: VYouResetPasswordParams) = client.resetPassword(params)

    // Refresh token
    suspend fun refreshToken(): VYouCredentials = client.refreshToken()

    // Profile
    suspend fun getProfile(): VYouProfile = client.getProfile()
    suspend fun editProfile(params: VYouEditProfileParams): VYouProfile = client.editProfile(params)

    /**
     * Builder class to initialize the client and create a single instance.
     * @param clientId The base64 string with 58 characters provided by VYou platform.
     * @param serverUrl The url with vyou-app domain provided by VYou platform.
     */
    class Builder(private val clientId: String, private val serverUrl: String, private val publicSaltBase64: String, private val appDeclaration: KoinAppDeclaration) {
        private var modules = mutableListOf<Module>()
        private var networkLogsLevel = VYouLogLevel.NONE
        private var onSignOut: () -> Unit = {}
        private var onRefreshTokenFailure: (VYouError) -> Unit = {}

        fun addModule(module: Module): Builder {
            modules.add(module)
            return this
        }

        fun addModules(list: List<Module>): Builder {
            modules.addAll(list)
            return this
        }

        fun enableNetworkLogs(level: VYouLogLevel): Builder {
            networkLogsLevel = level
            return this
        }

        fun addOnSignOut(block: () -> Unit): Builder {
            onSignOut = block
            return this
        }

        fun addOnRefreshTokenFailure(block: (VYouError) -> Unit): Builder {
            onRefreshTokenFailure = block
            return this
        }

        fun initialize() {
            validateClientId(clientId)
            validateServerUrl(serverUrl)

            instance = VYou(
                client = VYouClient(clientId, serverUrl, networkLogsLevel, onRefreshTokenFailure, onSignOut, modules, appDeclaration),
                crypt = Crypt(publicSaltBase64)
            )
        }

        private fun validateClientId(clientId: String) {
            when {
                clientId.isEmpty() -> throw VYouError(VYouErrorCode.INITIALIZE, "VYou.Builder clientId parameter must be non empty")
                clientId.length != CLIENT_ID_LENGTH -> throw VYouError(VYouErrorCode.INITIALIZE, "VYou.Builder clientId parameter must have 58 characters")
            }
        }

        private fun validateServerUrl(serverUrl: String) {
            when {
                serverUrl.isEmpty() -> throw VYouError(VYouErrorCode.INITIALIZE, "VYou.Builder serverUrl parameter must be non empty")
            }
        }

        private companion object {
            const val CLIENT_ID_LENGTH = 58
        }
    }

    companion object {
        private var instance: VYou? = null

        @Throws(VYouError::class)
        fun instance(): VYou {
            return instance ?: throw VYouError(VYouErrorCode.INSTANCE, "VYou.Builder::initialize() must be called before retrieve VYouClient instance")
        }
    }
}
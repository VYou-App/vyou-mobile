package com.vyou.kmm.client.common.di

import com.vyou.kmm.client.backend.BackendNetworkRepository
import com.vyou.kmm.client.backend.BackendRepository
import com.vyou.kmm.client.credentials.CredentialsRepository
import com.vyou.kmm.client.credentials.CredentialsStorageRepository
import com.vyou.kmm.client.profile.ProfileNetworkRepository
import com.vyou.kmm.client.profile.ProfileRepository
import com.vyou.kmm.client.refresh_token.RefreshTokenNetworkRepository
import com.vyou.kmm.client.refresh_token.RefreshTokenRepository
import com.vyou.kmm.client.reset_password.ResetPasswordNetworkRepository
import com.vyou.kmm.client.reset_password.ResetPasswordRepository
import com.vyou.kmm.client.sign_in.SignInNetworkRepository
import com.vyou.kmm.client.sign_in.SignInRepository
import com.vyou.kmm.client.sign_in_social.SignInSocialNetworkRepository
import com.vyou.kmm.client.sign_in_social.SignInSocialRepository
import com.vyou.kmm.client.sign_out.SignOutNetworkRepository
import com.vyou.kmm.client.sign_out.SignOutRepository
import com.vyou.kmm.client.sign_up.SignUpNetworkRepository
import com.vyou.kmm.client.sign_up.SignUpRepository
import com.vyou.kmm.client.tenant.TenantNetworkRepository
import com.vyou.kmm.client.tenant.TenantRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureModule = module {
    single<CredentialsRepository> { CredentialsStorageRepository(get()) }
    single<SignInRepository> { SignInNetworkRepository(get(named(SERVER_BASIC_NAME)), get(), get()) }
    single<SignInSocialRepository> { SignInSocialNetworkRepository(get(named(SERVER_BASIC_NAME)), get(), get()) }
    single<SignUpRepository> { SignUpNetworkRepository(get(named(SERVER_BASIC_NAME)), get(), get()) }
    single<SignOutRepository> { SignOutNetworkRepository(get(named(SERVER_OAUTH_NAME)), get()) }
    single<RefreshTokenRepository> { RefreshTokenNetworkRepository(get(named(SERVER_OAUTH_NAME)), get(), get()) }
    single<ResetPasswordRepository> { ResetPasswordNetworkRepository(get(named(SERVER_BASIC_NAME)), get()) }
    single<ProfileRepository> { ProfileNetworkRepository(get(named(SERVER_OAUTH_NAME)), get()) }
    single<TenantRepository> { TenantNetworkRepository(get(named(SERVER_BASIC_NAME)), get()) }
    single<BackendRepository> { BackendNetworkRepository(get(named(SERVER_BASIC_NAME)), get(), get()) }
}
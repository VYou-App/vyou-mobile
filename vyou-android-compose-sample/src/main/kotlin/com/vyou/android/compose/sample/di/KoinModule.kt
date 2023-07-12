package com.vyou.android.compose.sample.di

import com.vyou.android.compose.sample.R
import com.vyou.android.compose.sample.facebook.FacebookService
import com.vyou.android.compose.sample.facebook.FacebookSignInCollaborator
import com.vyou.android.compose.sample.feature.deeplink.DeeplinkNavigator
import com.vyou.android.compose.sample.feature.deeplink.DeeplinkViewModel
import com.vyou.android.compose.sample.feature.edit_profile.EditProfileNavigator
import com.vyou.android.compose.sample.feature.edit_profile.EditProfileViewModel
import com.vyou.android.compose.sample.feature.login.LoginNavigator
import com.vyou.android.compose.sample.feature.login.LoginViewModel
import com.vyou.android.compose.sample.feature.profile.ProfileNavigator
import com.vyou.android.compose.sample.feature.profile.ProfileViewModel
import com.vyou.android.compose.sample.feature.register.RegisterNavigator
import com.vyou.android.compose.sample.feature.register.RegisterViewModel
import com.vyou.android.compose.sample.feature.register_password.RegisterPasswordNavigator
import com.vyou.android.compose.sample.feature.register_password.RegisterPasswordViewModel
import com.vyou.android.compose.sample.feature.reset_password.ResetPasswordNavigator
import com.vyou.android.compose.sample.feature.reset_password.ResetPasswordViewModel
import com.vyou.android.compose.sample.feature.verify.VerifyNavigator
import com.vyou.android.compose.sample.feature.verify.VerifyViewModel
import com.vyou.android.compose.sample.google.GoogleService
import com.vyou.android.compose.sample.google.GoogleSignInCollaborator
import com.vyou.kmm.client.VYouField
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val googleModule = module {
    single { GoogleSignInCollaborator(androidContext(), androidContext().getString(R.string.vyou_google_server_id)) }
    singleOf(::GoogleService)
}

val facebookModule = module {
    single { FacebookSignInCollaborator(androidContext().getString(R.string.vyou_facebook_client_id)) }
    singleOf(::FacebookService)
}

val appModule = module {
    viewModel { (code: String, navigator: DeeplinkNavigator) -> DeeplinkViewModel(code, navigator) }
    viewModel { (fields: List<VYouField>, navigator: EditProfileNavigator) -> EditProfileViewModel(fields, navigator) }
    viewModel { (navigator: LoginNavigator) -> LoginViewModel(get(), get(), navigator) }
    viewModel { (navigator: ProfileNavigator) -> ProfileViewModel(navigator) }
    viewModel { (navigator: RegisterNavigator) -> RegisterViewModel(navigator) }
    viewModel { (navigator: RegisterPasswordNavigator) -> RegisterPasswordViewModel(navigator) }
    viewModel { (navigator: ResetPasswordNavigator) -> ResetPasswordViewModel(navigator) }
    viewModel { (email: String, navigator: VerifyNavigator) -> VerifyViewModel(email, navigator) }
}
package com.vyou.android.compose.sample.feature.deeplink

import androidx.lifecycle.viewModelScope
import com.vyou.kmm.client.VYouSignUpVerifyParams
import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import kotlinx.coroutines.launch

class DeeplinkViewModel(private val code: String, private val navigator: DeeplinkNavigator): BaseViewModel(navigator) {

    fun verify() {
        viewModelScope.launch {
            runCatching {
                val params = VYouSignUpVerifyParams(code)
                VYou.instance().signUpVerify(params)
            }.onSuccess {
                navigator.navigateToPassword(code)
            }.onFailure {
                navigator.navigateToLogin()
            }
        }
    }

    companion object {
        const val KEY_LAUNCHED_EFFECT = "deeplinkViewModel:key:launched_effect"
    }
}
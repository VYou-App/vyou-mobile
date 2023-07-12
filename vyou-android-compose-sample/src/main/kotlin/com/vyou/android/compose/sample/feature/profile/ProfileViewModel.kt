package com.vyou.android.compose.sample.feature.profile

import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.VYou
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(private val navigator: ProfileNavigator) : BaseViewModel(navigator) {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    suspend fun getProfile() {
        _state.update { it.copy(loading = true) }
        runCatching {
            VYou.instance().getProfile()
        }.onSuccess { profile ->
            _state.update { it.copy(profile = profile, loading = false) }
        }.onFailure { error ->
            _state.update { it.copy(loading = false, error = error.message) }
        }
    }

    fun editProfile() {
        state.value.profile?.let {
            navigator.editProfile(it.fields)
        }

    }

    fun logOut() = launch {
        _state.update { it.copy(logOutLoading = true) }
        runCatching {
            VYou.instance().signOut()
        }.onSuccess {
            _state.update { it.copy(logOutLoading = false) }
            navigator.logOut()
        }.onFailure { error ->
            _state.update { it.copy(logOutLoading = false, error = error.message) }
        }
    }
}
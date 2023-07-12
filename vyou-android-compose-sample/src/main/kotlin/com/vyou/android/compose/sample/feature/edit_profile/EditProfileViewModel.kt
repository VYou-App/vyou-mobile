package com.vyou.android.compose.sample.feature.edit_profile

import com.vyou.android.compose.sample.common.ui.BaseViewModel
import com.vyou.kmm.client.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel(fields: List<VYouField>, private val navigator: EditProfileNavigator): BaseViewModel(navigator) {

    private val _state = MutableStateFlow(EditProfileState(fields))
    val state = _state.asStateFlow()

    fun save() = launch {
        _state.update { it.copy(loading = true) }
        runCatching {
            val params = VYouEditProfileParams(state.value.fields)
            VYou.instance().editProfile(params)
        }.onSuccess {
            _state.update { it.copy(loading = false) }
            navigator.navigateToProfile()
        }.onFailure { error ->
            _state.update { it.copy(loading = false, error = error.message) }
        }
    }

    fun updateField(name: String, value: Any?) {
        val index = state.value.fields.indexOfFirst { it.name == name }
        val currentField = state.value.fields[index]
        val field = when (value) {
            is Number? -> VYouFieldNumber(value?.toDouble(), name, currentField.required, currentField.readOnly)
            else -> VYouFieldText(value?.toString(), name, currentField.required, currentField.readOnly)
        }
        _state.update { it.copy(
            fields = it.fields.map { original -> if(original.name == name) field else original }
        ) }
     }
}
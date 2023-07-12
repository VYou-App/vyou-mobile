package com.vyou.android.compose.sample.common.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


abstract class BaseViewModel(private val navigator: BaseNavigator): ViewModel() {
    protected fun launch(func: suspend () -> Unit) {
        viewModelScope.launch {
            func.invoke()
        }
    }

    fun popBack() {
        navigator.popBack()
    }
}
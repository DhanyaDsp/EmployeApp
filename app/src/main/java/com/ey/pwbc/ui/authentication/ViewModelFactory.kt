package com.ey.pwbc.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider




class ViewModelFactory(activity: LoginActivity):ViewModelProvider.Factory {
    private var loginViewModel:LoginViewModel? =null

    fun ViewModelFactory(viewModel: LoginViewModel) {
        this.loginViewModel = viewModel
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return loginViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
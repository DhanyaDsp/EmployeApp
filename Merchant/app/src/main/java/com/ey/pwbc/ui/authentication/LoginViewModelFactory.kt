package com.ey.pwbc.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ey.pwbc.Interface.LoginResultCallBacks

class LoginViewModelFactory(private val listener: LoginResultCallBacks) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel() as T
    }
}

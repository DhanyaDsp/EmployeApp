package com.ey.pwbc.ui.authentication

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.ey.pwbc.Interface.LoginResultCallBacks
import com.ey.pwbc.model.User
import androidx.lifecycle.MutableLiveData


class LoginViewModel : ViewModel() {
    private val user = MutableLiveData<User>()

    fun getUser(): MutableLiveData<User> {
        return user
    }

    fun onLoginClicked(user: User) {
        this.user.value = user;
    }

}
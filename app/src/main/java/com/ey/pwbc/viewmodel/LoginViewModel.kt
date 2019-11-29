package com.ey.pwbc.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import com.ey.pwbc.Interface.LoginResultCallBacks
import com.ey.pwbc.model.User

class LoginViewModel(private val listener: LoginResultCallBacks) : ViewModel() {
    private val user: User

    init {
        this.user = User("", "")
    }

    //fun to setUSername once user finish enter username
    val usernameTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user.setUserName(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }

    //fun to set password once user enter password
    val passwordTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user.setPassword(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }

    //fun for login button clicked
    fun onLoginClicked(view:View){
        if (user.isDataValid){
            listener.onSuccess("Login success")
        }else
            listener.onFailure("Login error")
    }

}
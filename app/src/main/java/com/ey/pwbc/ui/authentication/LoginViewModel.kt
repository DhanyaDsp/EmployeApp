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

    /*init {
        this.user = User("", "")
    }*/
    //fun for login button clicked
/*    fun onLoginClicked(view: View) {
        if (user.isDataValid) {
            listener.onSuccess("Login success")
        } else
            listener.onFailure("Login error")
    }*/

}
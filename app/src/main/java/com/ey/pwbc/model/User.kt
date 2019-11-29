package com.ey.pwbc.model

import android.text.TextUtils
import androidx.databinding.BaseObservable

class User(private var userName: String, private var password: String) : BaseObservable() {
    val isDataValid: Boolean
        get() = (getUserName().isNotEmpty())
                && getPassword().length > 6

    constructor() : this("", "")

    fun getPassword(): String {
        return password
    }

    fun getUserName(): String {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
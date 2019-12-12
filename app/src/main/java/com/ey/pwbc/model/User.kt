package com.ey.pwbc.model

import android.text.TextUtils
import androidx.databinding.BaseObservable

class User(
    private var userName: String,
    private var password: String,
    private var userType: Int
) : BaseObservable() {
    val isDataValid: Boolean
        get() = (getUserName().isNotEmpty())
                && getPassword().length > 6

    companion object {
        const val TYPE_MERCHANT = 1;
        const val TYPE_EMPLOYEE = 2;
        fun getFiscalCode(): String {
            return "LMTCLT70B45E472K"
        }

        fun getContractAddress(): String {
            return "add4285d4de32c6930224fd2a51f8c5773ff93dca71"
        }
    }

    constructor() : this("EMPLOYEE - 001", "abcdefg@001", User.TYPE_EMPLOYEE)
    //constructor() : this("MERCHANT - 001", "abcdefg@001", TYPE_MERCHANT)

    fun getPassword(): String {
        return password
    }

    fun getUserName(): String {
        return userName
    }

    fun getUserType(): Int {
        return userType
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setUserType(userType: Int) {
        this.userType = userType
    }

}
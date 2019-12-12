package com.ey.pwbc.Interface

interface LoginResultCallBacks {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}
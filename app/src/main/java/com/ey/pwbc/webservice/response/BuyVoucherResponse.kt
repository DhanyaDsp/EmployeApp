package com.ey.pwbc.webservice.response

import com.google.gson.annotations.SerializedName

class BuyVoucherResponse {
    private var message: String? = null

    @SerializedName("result")
    private var result: Boolean? = null

    @SerializedName("producthash")
    private var productHash: String? = null

    @SerializedName("pkEmployee")
    private var privateKey: String? = null

    public fun setProductHash(productHash: String) {
        this.productHash = productHash
    }

    public fun setPrivateKey(privateKey: String) {
        this.privateKey = privateKey
    }

    public fun setResult(result: Boolean) {
        this.result = result
    }

    public fun getProductHash(): String? {
        return productHash
    }


    public fun getPrivateKey(): String? {
        return privateKey
    }

    public fun isSuccess(): Boolean? {
        return result
    }

    fun setMessage(message: String) {
        this.message = message
    }
    fun getMessage(): String {
        return message!!
    }
}
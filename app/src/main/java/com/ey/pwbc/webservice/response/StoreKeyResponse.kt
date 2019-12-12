package com.ey.pwbc.webservice.response

import com.google.gson.annotations.SerializedName

class StoreKeyResponse {
    private var message: String? = null

    @SerializedName("result")
    private var result: Boolean? = null

    @SerializedName("codice-fiscale")
    private var fiscalCode: String? = null

    @SerializedName("pkEmployee")
    private var privateKey: String? = null

    public fun setFiscalCode(fiscalCode: String) {
        this.fiscalCode = fiscalCode
    }

    public fun setPrivateKey(privateKey: String) {
        this.privateKey = privateKey
    }

    public fun setResult(result: Boolean) {
        this.result = result
    }

    public fun getFiscalCode(): String? {
        return fiscalCode
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
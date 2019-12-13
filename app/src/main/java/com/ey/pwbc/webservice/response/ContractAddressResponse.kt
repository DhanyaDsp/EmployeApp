package com.ey.pwbc.webservice.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ContractAddressResponse {
    private var message: String? = null
    @SerializedName("result")
    @Expose
    private var result: Boolean? = null
    @SerializedName("contract-address")
    @Expose
    private var contractAddress: String? = null

    fun getResult(): Boolean? {
        return result
    }

    fun setResult(result: Boolean?) {
        this.result = result
    }

    fun getContractAddress(): String? {
        return contractAddress
    }

    fun setContractAddress(contractAddress: String) {
        this.contractAddress = contractAddress
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getMessage(): String {
        return message!!
    }

    public fun isSuccess(): Boolean? {
        return result
    }
}
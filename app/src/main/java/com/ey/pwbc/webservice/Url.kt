package com.ey.pwbc.webservice

import com.ey.pwbc.model.User

class Url {
    companion object {
        val BASE_URL: String = "http://cf15ce50.ngrok.io/postewelfare/REST/"
        val USER_TYPE = "employee/"
        val KEY_AUTHENTICATION =
            BASE_URL + USER_TYPE + "storePk/" + User.getFiscalCode() + "/" + User.getContractAddress() + "/"
        val GET_CONTRACT_ADDRESS = BASE_URL + USER_TYPE + "all/getContractAddress"
    }
}
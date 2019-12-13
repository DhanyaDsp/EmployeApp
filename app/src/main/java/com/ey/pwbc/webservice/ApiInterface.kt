package com.ey.pwbc.webservice

import com.ey.pwbc.webservice.response.ContractAddressResponse
import com.ey.pwbc.webservice.response.StoreKeyResponse
import retrofit2.Call
import retrofit2.http.*

public interface ApiInterface {

    //@Headers("Accept: application/json", "Content-Type: application/json")

//    @GET("http://cf15ce50.ngrok.io/postewelfare/REST/employee/storePk/LMTCLT70B45E472K/add4285d4de32c6930224fd2a51f8c5773ff93dca71")
//    fun getKeyDetails(): Call<StoreKeyResponse>

//    @GET
//    fun getKeyDetails(@retrofit2.http.Url url: String): Call<StoreKeyResponse>

    @GET("employee/storePk/{param1}/{param2}")
    fun getKeyDetails(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String): Call<StoreKeyResponse>

    @GET("all/getContractAddress/")
    fun getContractAddress(): Call<ContractAddressResponse>


}
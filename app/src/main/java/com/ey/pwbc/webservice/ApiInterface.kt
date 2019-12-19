package com.ey.pwbc.webservice

import com.ey.pwbc.webservice.response.BuyVoucherConfirmResponse
import com.ey.pwbc.webservice.response.BuyVoucherResponse
import com.ey.pwbc.webservice.response.ContractAddressResponse
import com.ey.pwbc.webservice.response.StoreKeyResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger

public interface ApiInterface {

    //@Headers("Accept: application/json", "Content-Type: application/json")

//    @GET("http://cf15ce50.ngrok.io/postewelfare/REST/employee/storePk/LMTCLT70B45E472K/add4285d4de32c6930224fd2a51f8c5773ff93dca71")
//    fun getKeyDetails(): Call<StoreKeyResponse>

//    @GET
//    fun getKeyDetails(@retrofit2.http.Url url: String): Call<StoreKeyResponse>

    @GET("postewelfare/REST/employee/storePk/{param1}/{param2}")
    fun getKeyDetails(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String): Call<StoreKeyResponse>

    @GET("postewelfare/REST/all/getContractAddress")
    fun getContractAddress(): Call<ContractAddressResponse>

    @GET("postewelfare/REST/employee/buyVoucherInit/{param1}/{param2}")
    fun buyVoucherInit(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String): Call<BuyVoucherResponse>

    @GET("postewelfare/REST/employee/buyVoucherConfirm/{param1}/{param2}/{param3}")
    fun confirmBuyVoucher(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String, @Path(value = "param3") param3: String): Call<BuyVoucherConfirmResponse>

    @GET("postewelfare/REST/employee/buyVoucherRollback/{param1}/{param2}")
    fun cancelBuyVoucher (@Path(value = "param1") param1: String,@Path(value = "param2")param2: String): Call<JsonObject>

    @GET("postewelfare/REST/employee/reedemVoucherInit/{param1}/{param2}")
    fun reedemVoucherInit(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String): Call<ContractAddressResponse>

    @GET("postewelfare/REST/employee/reedemVoucherConfirm/{param1}/{param2}")
    fun reedemVoucherConfirm(@Path(value = "param1") param1: String, @Path(value = "param2") param2: String): Call<ContractAddressResponse>

    @GET("postewelfare/REST/employee/buyVoucherRollback/{param1}/{param2}")
    fun reedemVoucherRollback (@Path(value = "param1") param1: String,@Path(value = "param2") param2: String): Call<ContractAddressResponse>
}
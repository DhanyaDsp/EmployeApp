package com.ey.pwbc.webservice

import com.ey.pwbc.Utils.HttpClientService
import com.ey.pwbc.webservice.Url.Companion.BASE_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    companion object {
        fun getClient(): Retrofit {
            return Retrofit.Builder().baseUrl(Url.BASE_URL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getKeyAuthenticationDetails(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        fun getContractAddress(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClientService.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

}
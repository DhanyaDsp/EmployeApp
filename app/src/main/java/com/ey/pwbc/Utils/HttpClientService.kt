package com.ey.pwbc.Utils

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import org.apache.http.conn.ssl.SSLSocketFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HttpClientService {
    companion object {
        fun getUnsafeOkHttpClient(): OkHttpClient {

            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                        //Do nothing
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                        //Do nothing
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                })
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(
                    null, trustAllCerts,
                    java.security.SecureRandom()
                )

                val sslSocketFactory = sslContext
                    .socketFactory

                return OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

//    companion object{
//        fun getUnsafeOkHttpClient() = OkHttpClient(){
//
//        }
//
//    }
}
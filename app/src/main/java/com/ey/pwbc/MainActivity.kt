package com.ey.pwbc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conio.postequorum.SDK
import com.conio.postequorum.implementation.Configuration
import com.conio.postequorum.implementation.SDKFactory
import com.ey.pwbc.Interface.LoginResultCallBacks


class MainActivity : AppCompatActivity(), LoginResultCallBacks {

    private lateinit var sdk: SDK
    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val conf = Configuration.ConfigurationBuilder(
            "0x7eCb3410eB7644076b2992E9DB4A9F12a4fed12C",
            "https://a0833b7c.ngrok.io"
        ).build()
        //Create SDK instance
        sdk = SDKFactory.getInstance().createSDK(conf)



       //To create keys:
        sdk = SDKFactory.getInstance().createSDK(conf);

       // To save keys:
        val SavedKeys = sdk.getKeyPair().serializePrivateKey();

       // To load a created account from keys:
        SDKFactory.getInstance().createSDK(conf);

        val pubkeyaddress = sdk.keyPair.noPrefixAddress
        Log.d("sos", "public key add$pubkeyaddress")


        //Save keys
        val keysToBeSavedInSecureStorage = sdk.keyPair.serializePrivateKey()
        Log.d("sos", "seed:$keysToBeSavedInSecureStorage")

        //create private/public keys
        val privateKey = sdk.keyPair?.serializePrivateKey()
        val publicKey = sdk.keyPair?.serializePublicKey()

        Log.d("sos", "public key:$publicKey")

        Log.d(
            "TAG",
            "private key " + android.util.Base64.encodeToString(
                privateKey,
                android.util.Base64.DEFAULT
            )
        )
        Log.d(
            "TAG",
            "public key  " + android.util.Base64.encodeToString(
                publicKey,
                android.util.Base64.DEFAULT
            )
        )
    }

}

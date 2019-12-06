package com.ey.pwbc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ey.pwbc.Interface.LoginResultCallBacks
import org.json.JSONObject
import org.web3j.crypto.CipherException
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException


class MainActivity : AppCompatActivity(), LoginResultCallBacks {
    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //create private/public keys
    private fun process(seed: String): JSONObject {

        val processJson = JSONObject()

        try {
            val ecKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec = ecKeyPair.privateKey
            Log.d("sos", "private key   $ecKeyPair")
            Log.d("sos", "private key privateKeyInDec  $privateKeyInDec")

            val sPrivatekeyInHex = privateKeyInDec.toString(16)
            Log.d("sos", "private key  $sPrivatekeyInHex")

            val aWallet = Wallet.createLight(seed, ecKeyPair)
            val sAddress = aWallet.address


            processJson.put("address", "0x$sAddress")
            processJson.put("privatekey", sPrivatekeyInHex)


        } catch (e: CipherException) {
            //
        } catch (e: InvalidAlgorithmParameterException) {
            //
        } catch (e: NoSuchAlgorithmException) {
            //
        } catch (e: NoSuchProviderException) {
            //
        }

        return processJson
    }

    fun createPrivateKey() {
        try {
            val password = "secr3t"
            val keyPair = Keys.createEcKeyPair()
            val wallet = Wallet.createStandard(password, keyPair)

            println("Priate key: " + keyPair.privateKey.toString(16))
            println("Account: " + wallet.address)

        } catch (e: Exception) {
            System.err.println("Error: " + e.message)
        }


    }

}

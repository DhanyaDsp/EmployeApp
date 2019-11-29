package com.ey.pwbc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ey.pwbc.Interface.LoginResultCallBacks
import com.ey.pwbc.databinding.ActivityLoginBinding
import com.ey.pwbc.ui.authentication.LoginViewModel
import org.json.JSONObject
import org.web3j.crypto.CipherException
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security.insertProviderAt
import java.security.Security.removeProvider
import java.security.Security


class MainActivity : AppCompatActivity(), LoginResultCallBacks {
    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBouncyCastle()
//        val seed = UUID.randomUUID().toString()
//        val result = process(seed) // get a json containing private key and address


/*
        val activityMainBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        activityMainBinding.viewModel = ViewModelProviders.of(this,
            LoginViewModelFactory(this)
        )
            .get(LoginViewModel::class.java)*/

        //createPrivateKey()
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

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass.equals(BouncyCastleProvider::class.java)) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        insertProviderAt(BouncyCastleProvider(), 1)
    }
}

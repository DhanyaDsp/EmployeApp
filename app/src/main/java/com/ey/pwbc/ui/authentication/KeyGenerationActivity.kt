package com.ey.pwbc.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.conio.postequorum.SDK
import com.conio.postequorum.implementation.Configuration
import com.conio.postequorum.implementation.SDKFactory

import com.ey.pwbc.R
import com.ey.pwbc.database.TokenDBManager
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.ui.dashboard.LandingActivity


class KeyGenerationActivity : AppCompatActivity() {
    lateinit var keyGenerateButton: Button
    private var privateKeyEditText: EditText? = null
    private var publicKeyEditText: EditText? = null
    private lateinit var sdk: SDK
    private var tokenRepo:TokenRepo?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_generation_activity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.app_name)
        privateKeyEditText = findViewById(R.id.edt_key_generation_primary_key)
        publicKeyEditText = findViewById(R.id.edt_key_generation_private_key)
        keyGenerateButton = findViewById(R.id.btn_generate_key)

        TokenDBManager.init(this)
        tokenRepo = TokenRepo()

        keyGenerateButton.setOnClickListener {

            val conf = Configuration.ConfigurationBuilder(
                "0x7eCb3410eB7644076b2992E9DB4A9F12a4fed12C",
                "https://a0833b7c.ngrok.io"
            ).build()
            //Create SDK instance
            sdk = SDKFactory.getInstance().createSDK(conf)

            //Save keys
            val keysToBeSavedInSecureStorage = sdk.keyPair.serializePrivateKey()
            Log.d("sos", "seed:$keysToBeSavedInSecureStorage")

            //create private/public keys
            val privateKey = sdk.keyPair?.serializePrivateKey()
            val publicKey = sdk.keyPair?.serializePublicKey()

            val decryptedPrivateKey = android.util.Base64.encodeToString(
                privateKey,
                android.util.Base64.DEFAULT
            )

            val decryptedPublicKey = android.util.Base64.encodeToString(
                publicKey,
                android.util.Base64.DEFAULT
            )

            Log.d(
                "TAG",
                "private key " + decryptedPrivateKey)
            Log.d(
                "TAG",
                "public key  " + decryptedPublicKey)

//            privateKeyEditText?.setText("POshFVljuyreDFkkkmhhgfTRQndggMNdddGVB")
//            publicKeyEditText?.setText("POshFVljuyreDFkkkmhhgfTRQndggMNdddGVB")



            privateKeyEditText?.setText(""+decryptedPrivateKey)
            publicKeyEditText?.setText(""+decryptedPublicKey)

            val tokenData = TokenData(privateKey.toString(), publicKey.toString())
            tokenRepo?.create(tokenData)

            successAlert("Le tue chiavi sono state generate con successo")
        }
    }

    private fun successAlert(message: String) {


        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(message)
            .setPositiveButton(
                "Ok"
            ) { _, _ ->

                intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)

            }
        val dialog_card = dialog.create()
        dialog_card.getWindow()?.setGravity(Gravity.BOTTOM);
        dialog_card.show();

    }
}
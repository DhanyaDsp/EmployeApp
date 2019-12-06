package com.ey.pwbc.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.ey.pwbc.R
import com.ey.pwbc.ui.dashboard.LandingActivity


class KeyGenerationActivity : AppCompatActivity() {
    lateinit var keyGenerateButton: Button
    private var privateKeyEditText: EditText? = null
    private var publicKeyEditText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_generation_activity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.app_name)
        privateKeyEditText = findViewById(R.id.edt_key_generation_primary_key)
        publicKeyEditText = findViewById(R.id.edt_key_generation_private_key)
        keyGenerateButton = findViewById(R.id.btn_generate_key)

        keyGenerateButton.setOnClickListener {
            privateKeyEditText?.setText("POshFVljuyreDFkkkmhhgfTRQndggMNdddGVB")
            publicKeyEditText?.setText("POshFVljuyreDFkkkmhhgfTRQndggMNdddGVB")
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
package com.ey.pwbc.ui.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.conio.postequorum.SDK
import com.conio.postequorum.implementation.Configuration
import com.conio.postequorum.implementation.SDKFactory

import com.ey.pwbc.R
import com.ey.pwbc.Utils.Utils
import com.ey.pwbc.database.TokenDBManager
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.ui.dashboard.LandingActivity
import com.ey.pwbc.webservice.*
import com.ey.pwbc.webservice.response.StoreKeyResponse
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.web3j.tuples.generated.Tuple2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


class KeyGenerationActivity : AppCompatActivity(), APICallback {


    private lateinit var keyGenerateButton: Button
    private var privateKeyEditText: EditText? = null
    private var publicKeyEditText: EditText? = null
    private lateinit var sdk: SDK
    private var tokenRepo: TokenRepo? = null
    private var userName: String? = null
    private var contractAddress: String? = null
    private var employeeAddress: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_generation_activity)
        if (intent != null) {
            userName = intent.getStringExtra("user_name")
            contractAddress = intent.getStringExtra("contractAddress")
            Log.d("sos", "getExtra contractAddress : $contractAddress")
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.app_name)
        privateKeyEditText = findViewById(R.id.edt_key_generation_primary_key)
        publicKeyEditText = findViewById(R.id.edt_key_generation_private_key)
        keyGenerateButton = findViewById(R.id.btn_generate_key)

        TokenDBManager.init(this)
        tokenRepo = TokenRepo()
        keyGenerateButton.setOnClickListener {
            initialiseSDK()
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

    private fun initialiseSDK() {
        val async = PostWelfareAsync()
        async.execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class PostWelfareAsync : AsyncTask<Void, Void, Tuple2<String, String>>() {
        override fun doInBackground(vararg params: Void?): Tuple2<String, String> {
            try {
                val conf = Utils.getConf()
                val retrievedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>
                if (retrievedPrivateKeyFromDB.isNotEmpty()) {
                    val privateKeyFromDB = retrievedPrivateKeyFromDB[0].privateKey
                    val publicKeyFromDB = retrievedPrivateKeyFromDB[0].publicKey
                    if (privateKeyFromDB != null) {
                        return Tuple2(privateKeyFromDB, publicKeyFromDB)
//                        val privateKeyByteArray: ByteArray = android.util.Base64.decode(privateKeyFromDB, android.util.Base64.DEFAULT)
//                        Log.d("sos", "private key 2nd time**:  $privateKeyFromDB")
//                        generateEmployeeAccountFromPrivateKey(privateKeyByteArray)
                    } else {
                        Toast.makeText(applicationContext, "Impossible case", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    return firstTimeSetup()
//                    generateEmployeeAccountFromPrivateKey(privateKeyByteArray)
                }


            } catch (e: Exception) {
                throw e
                Log.d("sos,", "AsyncTask exception:  ${e.localizedMessage}")
            }
            return Tuple2("", "")
        }

        private fun firstTimeSetup(): Tuple2<String, String> {
            sdk = SDKFactory.getInstance().createSDK(Utils.getConf())
            val privateKey = sdk.keyPair.serializePrivateKey();
            val publicKey = sdk.keyPair.serializePublicKey();

            val privateKeyAsString: String = android.util.Base64.encodeToString(
                privateKey,
                android.util.Base64.DEFAULT
            )
            val publicKeyAsString: String = android.util.Base64.encodeToString(
                publicKey,
                android.util.Base64.DEFAULT
            )
            Log.d("sos", "private key**:  $privateKeyAsString")
            Log.d("sos", "public key**:  $publicKeyAsString")

            val tokenSavedPrivateKeyData =
                TokenData(privateKeyAsString, publicKeyAsString)
            tokenRepo?.create(tokenSavedPrivateKeyData)

            getKeyAuthentication(object : APICallback {
                override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onProgress(requestCode: Int, isLoading: Boolean) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }, Utils.getEmployeeAddress(privateKey))
            return Tuple2(privateKeyAsString, publicKeyAsString)
        }

        override fun onPostExecute(result: Tuple2<String, String>) {
            super.onPostExecute(result)
            privateKeyEditText?.setText(result.component1())
            publicKeyEditText?.setText(result.component2())
            successAlert("Le tue chiavi sono state generate con successo")
        }
    }

    override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
        Toast.makeText(this, "Request success ", Toast.LENGTH_SHORT).show()
        Log.d("sos", "request code $requestCode")
    }

    override fun onFailure(requestCode: Int, obj: Any, code: Int) {
        Toast.makeText(this, "Request failed ", Toast.LENGTH_SHORT).show()
        Log.d("sos", "onFailure request code $requestCode")
    }

    override fun onProgress(requestCode: Int, isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun generateFiscalCode(userName: String): String {
        var fiscalCode = ""
        if (userName != null) {
            if (userName == "employee1") {
                fiscalCode = "RSSFLP58B10H501V"
                return fiscalCode
            } else if (userName == "employee2") {
                fiscalCode = "VRDVLR88B10H544X"
                return fiscalCode
            } else if (userName == "employee3") {
                fiscalCode = "BRBBNC72B10G100H"
                return fiscalCode
            } else {
                return "BRBBNC72B10G100H"
            }
        }
        return fiscalCode
    }

    private fun getKeyAuthentication(callBack: APICallback, employeeAddress: String) {
        val apiService: ApiInterface =
            ApiClient.getKeyAuthenticationDetails().create(ApiInterface::class.java)
        val call: Call<StoreKeyResponse> =
            apiService.getKeyDetails(
                generateFiscalCode(userName!!),
                employeeAddress
            )
        Log.d("sos", "call request: " + call.request().url())
        call.enqueue(object : Callback<StoreKeyResponse> {

            override fun onFailure(call: Call<StoreKeyResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong!  ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("sos", "onFailure " + t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StoreKeyResponse>,
                response: Response<StoreKeyResponse>
            ) {

                var storeKeyResponse: StoreKeyResponse = response.body()!!
                Log.d("sos", "response " + storeKeyResponse.isSuccess())

                Toast.makeText(
                    applicationContext,
                    "Associate key has been submitted ${storeKeyResponse.isSuccess()}",
                    Toast.LENGTH_LONG
                ).show()

                if (storeKeyResponse != null) {
                    if (storeKeyResponse.isSuccess()!!) {
                        callBack.onSuccess(102, storeKeyResponse, response.code())

                    } else {
                        storeKeyResponse.setMessage("Failed to get the Key Details!")
                        callBack.onFailure(
                            102, storeKeyResponse.getMessage(), response.code()
                        )
                    }
                } else {
                    storeKeyResponse = StoreKeyResponse();
                    storeKeyResponse.setMessage("Failed to load the Key Details!")
                    callBack.onFailure(
                        102,
                        storeKeyResponse.getMessage(),
                        response.code()
                    )
                }
            }
        })
    }
}
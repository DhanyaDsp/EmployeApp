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
import com.ey.pwbc.database.TokenDBManager
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.model.User
import com.ey.pwbc.ui.dashboard.LandingActivity
import com.ey.pwbc.webservice.*
import com.ey.pwbc.webservice.response.StoreKeyResponse
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.nio.charset.StandardCharsets


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
        initialiseSDK()


//        keyGenerateButton.setOnClickListener {
//
//            runOnUiThread {
//                if (employeeAddress != null)
//                    //getKeyAuthentication(this, employeeAddress!!)
//                successAlert("Le tue chiavi sono state generate con successo")
//            }
//        }

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
    inner class PostWelfareAsync : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {

            try {
                val conf = getConf()

                val retrivedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>
                if (retrivedPrivateKeyFromDB.size > 0) {
                    val privateKeyFromDB = retrivedPrivateKeyFromDB[0].privateKey
                    if (privateKeyFromDB != null) {
                        val privateKeyByteArray: ByteArray = privateKeyFromDB.toByteArray()
                        Log.d("sos", "private key 2nd time**:  $privateKeyFromDB")
                        generateEmployeeAccountFromPrivateKey(privateKeyByteArray)

                    } else {
                        Toast.makeText(applicationContext, "Impossible case", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val privateKeyByteArray = firstTimeSetup()
                    generateEmployeeAccountFromPrivateKey(privateKeyByteArray)
                }


            } catch (e: Exception) {
                throw e
                Log.d("sos,", "AsyncTask exception:  ${e.localizedMessage}")
            }


//            //  Calculate productHash from qrcode data:
//            val str = "0x20fB247Ced04CdA18eA78EE8D9Aa79948e516a8B"
//            var productHash = sdk.computeProductHash(
//                "LAVATRICE",
//                "987",
//                BigInteger.valueOf(str.toLong()),
//                BigInteger.valueOf(1576882799)
//            )
//
//            val productHashHex = ByteUtils.toHexString(productHash)
//
//            Log.d("sos","productHashHex $productHashHex")
//
//            sdk.dipendente().buyVoucher(productHash);


//            Log.d("sos ", "BigInteger: " + BigInteger("096057bc9b0ba0406d050d0b6866fad8ac64a588"))
//            var msg =  "Hi Techrocksz!";
//            BigInteger bi = new BigInteger(msg.getBytes());
//            System.out.println(new String (bi.toByteArray()));

            return null

        }

        private fun firstTimeSetup(): ByteArray {
            sdk = SDKFactory.getInstance().createSDK(getConf())
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
//            val privateKeyAsString: String = String(privateKey, StandardCharsets.UTF_8)
//            val publicKeyAsString: String = String(publicKey, StandardCharsets.UTF_8)
            Log.d("sos", "private key**:  $privateKeyAsString")
            Log.d("sos", "public key**:  $publicKeyAsString")

            val tokenSavedPrivateKeyData =
                TokenData(privateKeyAsString, publicKeyAsString)
            tokenRepo?.create(tokenSavedPrivateKeyData)

            val retrivedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>
            val privateKeyFromDB = retrivedPrivateKeyFromDB[0].privateKey
            val publicKeyFromDB = retrivedPrivateKeyFromDB[0].publicKey
            Log.d("sos", "DB retrival private key**:  $privateKeyFromDB")
            Log.d("sos", "DB retrival public key**:  $publicKeyFromDB")
            return privateKey
        }

        override fun onPostExecute(result: Void?) {

            super.onPostExecute(result)
        }
    }

    private fun getConf(): Configuration? {
        return Configuration.ConfigurationBuilder(
            "0xa07A306235ef748EC11cFfd373d9B9a21010A522",
            "http://postewelfareapi.westeurope.cloudapp.azure.com:22100/"
        ).build()
    }

    private fun generateEmployeeAccountFromPrivateKey(privateKey: ByteArray) {
        val sdkTempEmployeeAccount =
            SDKFactory.getInstance().createSDK(privateKey, getConf())
        val employeeAddress = sdkTempEmployeeAccount.keyPair.noPrefixAddress
        Log.d("sos", "employeeAddress**:   $employeeAddress")
        val tokenBalance = sdkTempEmployeeAccount.myTokenBalance()
        Log.d("sos", "tokenBalance**:   $tokenBalance")

        val voucherBalance = sdkTempEmployeeAccount.myVouchersBalance()
        val employeeVoucherList = sdkTempEmployeeAccount.myVouchersList()

        Log.d("sos", "voucherBalance**:   ${voucherBalance.component1().toString()}")
        Log.d("sos", "employeeVoucherList**:   ${employeeVoucherList.get(0).toString()}")
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

}
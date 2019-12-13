package com.ey.pwbc.ui.authentication

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_generation_activity)
        if (intent != null) {
            userName = intent.getStringExtra("user_name")
            contractAddress = intent.getStringExtra("contractAddress")
            Log.d("sos", "getExtra contractAddress : $contractAddress")
        }
        initialiseSDK()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.title = getString(R.string.app_name)
        privateKeyEditText = findViewById(R.id.edt_key_generation_primary_key)
        publicKeyEditText = findViewById(R.id.edt_key_generation_private_key)
        keyGenerateButton = findViewById(R.id.btn_generate_key)

        TokenDBManager.init(this)
        tokenRepo = TokenRepo()
        getKeyAuthentication(this)


        keyGenerateButton.setOnClickListener {

            runOnUiThread {
                successAlert("Le tue chiavi sono state generate con successo")
                //getKeyAuthentication(this)
            }
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

    private fun getKeyAuthentication(callBack: APICallback) {
        val apiService: ApiInterface =
            ApiClient.getKeyAuthenticationDetails().create(ApiInterface::class.java)
        val call: Call<StoreKeyResponse> =
            apiService.getKeyDetails(generateFiscalCode(userName!!), "15808f10cacc31df73344c4a56c1de89127e0f22")
        Log.d("sos", "call request: " + call.request().url())
        call.enqueue(object : Callback<StoreKeyResponse> {

            override fun onFailure(call: Call<StoreKeyResponse>, t: Throwable) {

                Log.d("sos", "onFailure " + t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StoreKeyResponse>,
                response: Response<StoreKeyResponse>
            ) {

                var storeKeyResponse: StoreKeyResponse = response.body()!!
                Log.d("sos", "response " + storeKeyResponse.isSuccess())
                successAlert("" + storeKeyResponse)

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

    private fun initialiseSDK() {
        val conf = Configuration.ConfigurationBuilder(
            "0x44C7174DA81681115d01F0340D695DC26FB8C38F",
            "https://18.223.41.243"
        ).build()
        //Create SDK instance
        sdk = SDKFactory.getInstance().createSDK(conf)

        //To save keys:
        val savedPrivateKey = sdk.keyPair.serializePrivateKey();
        val savedPublicKey = sdk.keyPair.serializePublicKey();

        privateKeyEditText?.setText("" + savedPrivateKey)
        publicKeyEditText?.setText("" + savedPublicKey)
        val tokenData = TokenData(savedPrivateKey.toString(), savedPublicKey.toString())
        tokenRepo?.create(tokenData)

        val sdkMerchantAccount = SDKFactory.getInstance().createSDK(savedPrivateKey, conf)
        val employeeAddress = sdkMerchantAccount.keyPair.noPrefixAddress



        Log.d("sos", "public key address from Employee SDK $employeeAddress")



        val publicKeyAddress = sdk.keyPair.noPrefixAddress
        Log.d("sos", "public key address from SDK $publicKeyAddress")

        val async = postWelfareAync()
        async.execute()
    }

    inner class postWelfareAync : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {

            try{
                val tokenBalance = sdk.myTokenBalance()

                // Retrive voucher balance:
                val voucherBalance = sdk.myVouchersBalance();

                //Retrive voucher list:
                val voucherList = sdk.myVouchersList();

                Log.d("sos,", "token balance: $tokenBalance ")
                Log.d("sos,", "voucherBalance : $voucherBalance")
                Log.d("sos,", "voucherList: $voucherList")
            }catch (e:Exception){
                Log.d("sos,", "excption:  ${e.message}")
            }
            // Retrive welfare token balance:


            // Calculate productHash from qrcode data:
//            val myInteger = 20
            // var productHash = sdk.computeProductHash("Cappello-Bianco-Adidas", BigInteger.valueOf(Adidas-Store), duedate);
//            var bigIntegerStr: BigInteger = BigInteger("096057bc9b0ba0406d050d0b6866fad8ac64a588");


//            Log.d("sos ", "BigInteger: " + BigInteger("096057bc9b0ba0406d050d0b6866fad8ac64a588"))
//            var msg =  "Hi Techrocksz!";
//            BigInteger bi = new BigInteger(msg.getBytes());
//            System.out.println(new String (bi.toByteArray()));

            return null

        }

        override fun onPostExecute(result: Void?) {

            super.onPostExecute(result)
        }
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
            }
        }
        return fiscalCode
    }

    override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
        Toast.makeText(this,"Request success ",Toast.LENGTH_SHORT).show()

        Log.d("sos", "request code $requestCode")
    }

    override fun onFailure(requestCode: Int, obj: Any, code: Int) {
        Toast.makeText(this,"Request failed ",Toast.LENGTH_SHORT).show()

        Log.d("sos", "onFailure request code $requestCode")
    }

    override fun onProgress(requestCode: Int, isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
package com.ey.pwbc.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.conio.postequorum.implementation.SDKFactory
import com.ey.pwbc.R
import com.ey.pwbc.Utils.Utils
import com.ey.pwbc.UtilsDialog
import com.ey.pwbc.database.TokenDBManager
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.databinding.ActivityVoucherDetailBinding
import com.ey.pwbc.model.ScanData
import com.ey.pwbc.model.User
import com.ey.pwbc.ui.dashboard.LandingActivity
import com.ey.pwbc.webservice.APICallback
import com.ey.pwbc.webservice.ApiClient
import com.ey.pwbc.webservice.ApiInterface
import com.ey.pwbc.webservice.response.ContractAddressResponse
import kotlinx.android.synthetic.main.app_bar_landing.*
import org.web3j.tuples.generated.Tuple2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


class VoucherDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: VoucherDetailViewModel
    private lateinit var binding: ActivityVoucherDetailBinding
    private var deepLinkData: ScanData? = null
    private var privateKey: ByteArray? = null
    private var position: Int? = null
    private var voucherId: String? = null
    private var merchant: String? = null
    private var merchantPublicKey: String? = null
    private var voucherValueFromList: String? = null
    private var merchant_address: String? = null
    var toolbar: Toolbar? = null
    val user = User
    private var et_name: EditText? = null
    private var et_value: EditText? = null
    private var et_merchant: EditText? = null
    private var et_deadline: EditText? = null
    private var tv_performedby_name: EditText? = null
    private var tokenRepo: TokenRepo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        et_name = findViewById(R.id.et_name)
        et_value = findViewById(R.id.et_value)
        et_merchant = findViewById(R.id.et_merchant)
        et_deadline = findViewById(R.id.et_deadline)
        tv_performedby_name = findViewById(R.id.tv_performedby_name)
        TokenDBManager.init(this)
        tokenRepo = TokenRepo()
        privateKey = getPrivateKeyFromDB()

        Log.d("sos", "db $privateKey")
        if (intent != null) {
            val intent = intent

            val voucherNameFromList = intent.getStringExtra("voucher_name")
            voucherValueFromList = intent.getStringExtra("voucher_value")
            merchant = intent.getStringExtra("merchant")
            val voucherDateFromList = intent.getStringExtra("voucher_date")
            // val voucherIdLocal = intent.getSerializableExtra("voucher_id")!!.toString()
            voucherId = intent.getSerializableExtra("voucher_id")!!.toString()
            merchant_address = intent.getSerializableExtra("merchant_address")!!.toString()
            position = intent.getIntExtra("position", 0)
            merchantPublicKey = voucherValueFromList?.substring(2, voucherValueFromList!!.length)
            Log.d("sos", "merchantPublicKey: $merchantPublicKey")


            //Retrieve from SharedPreference
            val preference =
                getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
            val username = preference.getString("username", "")

            val scanData = ScanData(
                voucherNameFromList!!,
                voucherValueFromList!!,
                merchant!!,
                voucherDateFromList!!,
                username!!
            )

            if (intent.getStringExtra("voucher_name") != null) {
                deepLinkData = scanData
                et_name?.setText(scanData.name)
                et_value?.setText(scanData.value)
                et_merchant?.setText(scanData.merchant)
                et_deadline?.setText(scanData.date)
            }


            /* et_name?.setText(scanData.name)

               et_value?.setText(scanData.value)

               et_merchant?.setText(scanData.merchant)

               et_deadline?.setText(scanData.date)

               val prod = intent.data.toString()
               Log.d("sos", "data:  ${intent.data}")
               val array = prod.split(",")
               if (intent.data == null) {
                   val scanData = ScanData("", "20 WT", "Adidas Store", "21/12/2019")

                   deepLinkData = scanData

                   et_name?.setText(scanData.name)
                   et_value?.setText(scanData.value)
                   et_merchant?.setText(scanData.merchant)
                   et_deadline?.setText(scanData.date)


               } else {

                   val scanData = ScanData(array[0], array[1], array[2], array[3])

                   deepLinkData = scanData

                   et_name?.setText(scanData.name)
                   et_value?.setText(scanData.value)
                   et_merchant?.setText(scanData.merchant)
                   et_deadline?.setText(scanData.date)
               }*/


        }


        initBinding()
        initToolbar()

    }


    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        tvTitle.text = getString(R.string.voucher_title)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voucher_detail)
        binding.lifecycleOwner = this
        binding.deepLinkData = deepLinkData
        viewModel = ViewModelProviders.of(this).get(VoucherDetailViewModel::class.java)
        binding.voucherDetailViewModel = viewModel
        //if we need to update the voucher fields from the edittext then we can add a voucher model here and bind it with layout.

    }

    fun hideToolbar() {
        toolbar?.visibility = View.GONE
    }

    fun onVoucherRedeemClick(view: View) {
        viewModel.onVoucherClick1()

        UtilsDialog.showAlert(
            "ll  tuo Voucher verra trasferito ad Adidas Store",
            "Confermi?",
            this,
            View.OnClickListener {
                reedemVoucherApi(object : APICallback {
                    override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                        Log.d("sos", "s1")
                        RedeemVoucherAsync().execute()
                    }

                    override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                        Log.d("sos", "f1")
                    }

                    override fun onProgress(requestCode: Int, isLoading: Boolean) {
                        Log.d("sos", "p1")
                    }

                }, BigInteger(voucherId!!).toString(16), merchantPublicKey!!)
            },
            View.OnClickListener { },
            View.OnClickListener { },
            UtilsDialog.TYPE_CONFIRM
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.landing, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveToPostRedeemVoucher() {
        val redeemVoucherView =
            this.findViewById<ConstraintLayout>(R.id.view_post_redeem_voucher)
        val closeImageView = this.findViewById<ImageView>(R.id.ivClose)



        redeemVoucherView.visibility = View.VISIBLE
        closeImageView.setOnClickListener {
            //  hideToolbar()
            intent = Intent(this, LandingActivity::class.java)
            startActivityForResult(intent, 101)
            //startActivity(intent)
            //redeemVoucherView.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getPrivateKeyFromDB(): ByteArray {
        val retrievedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>
        val privateKeyFromDB = retrievedPrivateKeyFromDB[0].privateKey
        return Base64.decode(privateKeyFromDB, Base64.DEFAULT)

    }

    private fun getEmployeeAddressFromDB(): String {
        return Utils.getEmployeeAddress(privateKey)
    }

    private fun reedemVoucherApi(
        callBack: APICallback,
        voucherId: String,
        merchantKey: String
    ) {
        //Api call
        val apiService: ApiInterface =
            ApiClient.confirmBuyVoucher().create(ApiInterface::class.java)
        val call: Call<ContractAddressResponse> =
            apiService.reedemVoucherInit(
                voucherId,
                merchantKey
            )
        Log.d("sos", "reedem request: " + call.request().url())
        call.enqueue(object : Callback<ContractAddressResponse> {

            override fun onFailure(call: Call<ContractAddressResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ContractAddressResponse>,
                response: Response<ContractAddressResponse>
            ) {
                val contractAddressResponse: ContractAddressResponse = response.body()!!
                callBack.onSuccess(111, contractAddressResponse, response.code())

            }
        })
    }

    private fun confirmRedeemVoucherApi(
        callBack: APICallback,
        voucherId: String,
        merchantKey: String
    ) {
        //Api call
        val apiService: ApiInterface =
            ApiClient.reedemVoucherConfirm().create(ApiInterface::class.java)
        val call: Call<ContractAddressResponse> =
            apiService.reedemVoucherConfirm(
                voucherId,
                merchantKey
            )
        Log.d("sos", "reedem confirm request: " + call.request().url())
        call.enqueue(object : Callback<ContractAddressResponse> {

            override fun onFailure(call: Call<ContractAddressResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ContractAddressResponse>,
                response: Response<ContractAddressResponse>
            ) {
                val contractAddressResponse: ContractAddressResponse = response.body()!!
                callBack.onSuccess(111, contractAddressResponse, response.code())

            }
        })
    }

    private fun rollBackRedeemVoucherApi(
        callBack: APICallback,
        voucherId: String,
        employeeAddress: String
    ) {
        //Api call
        val apiService: ApiInterface =
            ApiClient.reedemVoucherRollback().create(ApiInterface::class.java)
        val call: Call<ContractAddressResponse> =
            apiService.reedemVoucherRollback(
                voucherId,
                employeeAddress
            )
        Log.d("sos", "reedem rollback request: " + call.request().url())
        call.enqueue(object : Callback<ContractAddressResponse> {

            override fun onFailure(call: Call<ContractAddressResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ContractAddressResponse>,
                response: Response<ContractAddressResponse>
            ) {
                val contractAddressResponse: ContractAddressResponse = response.body()!!
                callBack.onSuccess(111, contractAddressResponse, response.code())

            }
        })
    }

    private fun useVoucher() {
        val sdk = SDKFactory.getInstance().createSDK(getPrivateKeyFromDB(), Utils.getConf())

        sdk.dipendente().redeemVoucher(BigInteger(voucherId!!))
        confirmRedeemVoucherApi(object : APICallback {
            override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                Log.d("sos", "s1")
                moveToPostRedeemVoucher()
            }

            override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                Log.d("sos", "f1")
            }

            override fun onProgress(requestCode: Int, isLoading: Boolean) {
                Log.d("sos", "p1")
            }

        }, BigInteger(voucherId!!).toString(16), merchantPublicKey!!)
    }

    @SuppressLint("StaticFieldLeak")
    inner class RedeemVoucherAsync : AsyncTask<Void, Void, Tuple2<String, String>>() {
        override fun doInBackground(vararg params: Void?): Tuple2<String, String> {
            try {
                useVoucher()
            } catch (e: Exception) {
                rollBackRedeemVoucherApi(object : APICallback {
                    override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                        Log.d("sos", "s1")
                        // RedeemVoucherAsync().execute()
                    }

                    override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                        Log.d("sos", "f1")
                    }

                    override fun onProgress(requestCode: Int, isLoading: Boolean) {
                        Log.d("sos", "p1")
                    }

                }, BigInteger(voucherId!!).toString(16), getEmployeeAddressFromDB())
                Toast.makeText(
                    applicationContext,
                    "Something went wrong!  ${e.message}", Toast.LENGTH_LONG
                ).show()
                Log.d("sos,", "AsyncTask exception:  ${e.localizedMessage}")
            }
            return Tuple2("", "")
        }

        override fun onPostExecute(result: Tuple2<String, String>) {
            super.onPostExecute(result)
        }
    }

}
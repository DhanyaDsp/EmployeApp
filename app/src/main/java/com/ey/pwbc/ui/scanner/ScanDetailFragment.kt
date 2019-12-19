package com.ey.pwbc.ui.scanner

import android.content.Context
import android.opengl.Visibility
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.conio.postequorum.implementation.SDKFactory

import com.ey.pwbc.R
import com.ey.pwbc.Utils.Utils
import com.ey.pwbc.database.TokenDBManager
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.databinding.ScanDetailFragmentBinding
import com.ey.pwbc.model.ScanData
import com.ey.pwbc.model.ScanStatus
import com.ey.pwbc.viewmodel.ScanDetailViewModel
import com.ey.pwbc.webservice.APICallback
import com.ey.pwbc.webservice.ApiClient
import com.ey.pwbc.webservice.ApiInterface
import com.ey.pwbc.webservice.response.BuyVoucherConfirmResponse
import com.ey.pwbc.webservice.response.BuyVoucherResponse
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.scan_detail_fragment.view.*
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.bouncycastle.util.encoders.Hex
import org.web3j.tuples.generated.Tuple2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class ScanDetailFragment : Fragment() {
    private var tokenRepo: TokenRepo? = null
    private var productHashHex: String? = null
    private var voucherId: BigInteger? = null
    private var privateKey: ByteArray? = null
    private var employeeAddress: String? = "8f1ca25eea88462861d9253de1de5995d5254302"

    companion object {
        fun newInstance() = ScanDetailFragment()
        @JvmStatic
        fun newInstance(scanData: ScanData) = ScanDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("scanData", scanData)
            }
        }
    }

    private var scanData: ScanData? = null
    private lateinit var viewModel: ScanDetailViewModel
    private lateinit var binding: ScanDetailFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.scan_detail_fragment, container, false)
        binding.lifecycleOwner = this
        binding.scanData = scanData;
        val root = binding.root
        root.btnProceed.setOnClickListener {
            PostWelfareAsync().execute()
        }
        TokenDBManager.init(activity)
        tokenRepo = TokenRepo()

        privateKey = getPrivateKeyFromDB()
        Log.d("sos", "db $privateKey")
        return root;
    }

    private fun moveToPostScanScreen(status: Int) {
        val navController = activity?.findNavController(R.id.nav_host_fragment)
        var bundle = bundleOf("scanStatus" to ScanStatus(status, "", ""))
        navController?.navigate(R.id.nav_post_scan, bundle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getSerializable("scanData")?.let {
            this.scanData = it as ScanData;
        }
    }

    private fun scanProduct() {

        var root = binding.root.progressBar
        activity?.runOnUiThread { root.visibility = View.VISIBLE }
        val sdk = SDKFactory.getInstance().createSDK(getPrivateKeyFromDB(), Utils.getConf())

        val productName = scanData?.name
        val productValue = scanData?.value
        val productDate = scanData?.date
        val merchantAddress = scanData?.merchant

        val productHash = sdk.computeProductHash(
            productName,
            merchantAddress,
            BigInteger(productValue!!),
            BigInteger(productDate!!)
        )

        productHashHex = ByteUtils.toHexString(productHash)
        //  val productHashHex = Utils.bytesToHex(productHash)
        Log.d("sos", "productHashHex $productHashHex")
        //val productHashHexNew = String(Hex.encode(productHash))

        // api call after calculating product hash:
        // val privateKeyAsByteArray = getPrivateKeyFromDB()
        buyVoucherInitAPI(object : APICallback {

            override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                try {
                    val buyVoucherOperation = sdk.dipendente().buyVoucher(productHash)
                    Log.d("sos", "buyVoucherOperation :$buyVoucherOperation")
                    activity?.runOnUiThread { root.visibility = View.INVISIBLE }


//                confirm api
                    confirmVoucherApi(
                        object : APICallback {
                            override fun onSuccess(requestCode: Int, obj: Any, code: Int) {
                                moveToPostScanScreen(0)
                                Log.d("sos", "confirmVoucherApi success :")
                            }

                            override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                                moveToPostScanScreen(1)
                                Log.d("sos", "confirmVoucherApi failed :")
                            }

                            override fun onProgress(requestCode: Int, isLoading: Boolean) {
                                Log.d("sos", "confirmVoucherApi onProgress :")
                            }

                        },
                        productHashHex,
                        "8f1ca25eea88462861d9253de1de5995d5254302",
                        buyVoucherOperation!!
                    )

                } catch (e: Exception) {
                    activity?.runOnUiThread { root.visibility = View.INVISIBLE }
                    Log.d("sos", "buy voucher operation exception: " + e.localizedMessage)
                    moveToPostScanScreen(1)
                    //call this api when blockchain operation fails-rollback api
                    val apiService: ApiInterface =
                        ApiClient.cancelBuyVoucher().create(ApiInterface::class.java)
                    val call: Call<JsonObject> =
                        apiService.cancelBuyVoucher(productHashHex!!)
                    Log.d("sos", "URL: " + call.request().url())

                    call.enqueue(object : Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.d("sos", "rollback buy voucher failure :" + t.message)
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            Log.d("sos", "rollback buy voucher response :" + response.isSuccessful)
                        }

                    })
                    // Toast.makeText(activity, "Exception:  " + e.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(requestCode: Int, obj: Any, code: Int) {
                moveToPostScanScreen(1)
                Log.d("sos", "onFailure request code: $requestCode")
            }

            override fun onProgress(requestCode: Int, isLoading: Boolean) {
                Log.d("sos", " onProgress request code: $requestCode")
            }

        }, productHashHex, employeeAddress!!)


    }

    private fun buyVoucherInitAPI(
        callBack: APICallback,
        productHashHex: String?,
        employeeAddress: String
    ) {
        val apiService: ApiInterface =
            ApiClient.buyVoucher().create(ApiInterface::class.java)
        val call: Call<BuyVoucherResponse> =
            apiService.buyVoucherInit(
                productHashHex!!,
                employeeAddress
            )
        Log.d("sos", "call request: " + call.request().url())
        call.enqueue(object : Callback<BuyVoucherResponse> {

            override fun onFailure(call: Call<BuyVoucherResponse>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong!  ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("sos", "onFailure " + t.localizedMessage)
            }

            override fun onResponse(
                call: Call<BuyVoucherResponse>,
                response: Response<BuyVoucherResponse>
            ) {

                var buyVoucherResponse: BuyVoucherResponse = response.body()!!
                Log.d("sos", "response " + buyVoucherResponse.isSuccess())

                Toast.makeText(
                    activity,
                    "Buy voucher:  ${buyVoucherResponse.isSuccess()}",
                    Toast.LENGTH_LONG
                ).show()


                if (buyVoucherResponse != null) {
                    if (buyVoucherResponse.isSuccess()!!) {


                        callBack.onSuccess(102, buyVoucherResponse, response.code())

                        //TODO:call confirm api.

                    } else {
                        buyVoucherResponse.setMessage("Failed to buy voucher!")
                        callBack.onFailure(
                            102, buyVoucherResponse.getMessage(), response.code()
                        )
                    }
                } else {
                    buyVoucherResponse = BuyVoucherResponse();
                    buyVoucherResponse.setMessage("Failed buy voucher!")
                    callBack.onFailure(
                        102,
                        buyVoucherResponse.getMessage(),
                        response.code()
                    )
                }
            }
        })


    }

    inner class PostWelfareAsync : AsyncTask<Void, Void, Tuple2<String, String>>() {
        override fun doInBackground(vararg params: Void?): Tuple2<String, String> {
            try {
                scanProduct()
            } catch (e: Exception) {
                throw e
                Log.d("sos,", "AsyncTask exception:  ${e.localizedMessage}")
            }
            return Tuple2("", "")
        }

        override fun onPostExecute(result: Tuple2<String, String>) {
            super.onPostExecute(result)

        }
    }

    private fun getPrivateKeyFromDB(): ByteArray {
        //the below code is crashing bcs null.
        val retrievedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>

        val privateKeyFromDB = retrievedPrivateKeyFromDB[0].privateKey
        return Base64.decode(privateKeyFromDB, Base64.DEFAULT)

    }

    private fun confirmVoucherApi(
        callBack: APICallback,
        productHashHex: String?,
        employeeAddress: String,
        voucherId: BigInteger
    ) {
        //Api call
        val apiService: ApiInterface =
            ApiClient.confirmBuyVoucher().create(ApiInterface::class.java)
        val call: Call<BuyVoucherConfirmResponse> =
            apiService.confirmBuyVoucher(
                productHashHex!!,
                employeeAddress, voucherId
            )
        Log.d("sos", "call request: " + call.request().url())
        call.enqueue(object : Callback<BuyVoucherConfirmResponse> {

            override fun onFailure(call: Call<BuyVoucherConfirmResponse>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Something went wrong!  ${t.message}", Toast.LENGTH_LONG
                ).show()
                moveToPostScanScreen(1)
            }

            override fun onResponse(
                call: Call<BuyVoucherConfirmResponse>,
                response: Response<BuyVoucherConfirmResponse>
            ) {

                var buyVoucherResponse: BuyVoucherConfirmResponse = response.body()!!
                Log.d("sos", "response " + buyVoucherResponse.isSuccess())


                Toast.makeText(
                    activity,
                    "Buy voucher:  ${buyVoucherResponse.isSuccess()}",
                    Toast.LENGTH_LONG
                ).show()

                if (buyVoucherResponse != null) {
                    if (buyVoucherResponse.isSuccess()!!) {
                        callBack.onSuccess(102, buyVoucherResponse, response.code())
                        moveToPostScanScreen(0)

                    } else {
                        buyVoucherResponse.setMessage("Failed to buy voucher!")
                        callBack.onFailure(
                            102, buyVoucherResponse.getMessage(), response.code()
                        )
                        moveToPostScanScreen(0)
                    }
                } else {
                    buyVoucherResponse = BuyVoucherConfirmResponse();
                    buyVoucherResponse.setMessage("Failed buy voucher!")
                    callBack.onFailure(
                        102,
                        buyVoucherResponse.getMessage(),
                        response.code()
                    )
                }
            }
        })
    }
}

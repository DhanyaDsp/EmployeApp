package com.ey.pwbc.ui.scanner

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.scan_detail_fragment.view.*
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.web3j.tuples.generated.Tuple2
import java.math.BigInteger

class ScanDetailFragment : Fragment() {
    private var tokenRepo: TokenRepo? = null

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
            moveToPostScanScreen();
        }
        TokenDBManager.init(activity)
        tokenRepo = TokenRepo()
        return root;
    }

    private fun moveToPostScanScreen() {
        val navController = activity?.findNavController(R.id.nav_host_fragment)
        var bundle = bundleOf("scanStatus" to ScanStatus(ScanStatus.STATUS_SUCCESS, "", ""))
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
            PostWelfareAsync().execute()
        }
    }

    private fun scanProduct() {

        val sdk = SDKFactory.getInstance().createSDK(getPrivateKeyFromDB(), Utils.getConf())

        var productHash = sdk.computeProductHash(
            "LAVATRICE",
            "d91960fe42fac3b6731373ad51ca6605fb8ec739",
            BigInteger("987"),
            BigInteger("1576882799")
        )
        val productHashHex = ByteUtils.toHexString(productHash)
        Log.d("sos", "productHashHex $productHashHex")


        // api call
       /* val privateKeyAsByteArray = getPrivateKeyFromDB()
        buyVoucherInitAPI(this,productHashHex,Utils.getEmployeeAddress(privateKeyAsByteArray) )
*/

//        sdk.dipendente().buyVoucher(productHash)
        val tokenBalance = sdk.myTokenBalance()
        Log.d("sos", "tokenBalance**:   $tokenBalance")
        val voucherBalance = sdk.myVouchersBalance()
        val employeeVoucherList = sdk.myVouchersList()
        Log.d("sos", "voucherBalance1**:   ${voucherBalance.component1().toString()}")
        Log.d("sos", "voucherBalance2**:   ${voucherBalance.component1().toString()}")
        Log.d("sos", "employeeVoucherList**:   ${employeeVoucherList.get(0).toString()}")
        Log.d("sos", "employeeVoucherList size**:   ${employeeVoucherList.size}")
//            BigInteger bi = new BigInteger(msg.getBytes());
//            System.out.println(new String (bi.toByteArray()));
    }

    private fun buyVoucherInitAPI(callBack:APICallback,productHashHex: String?, employeeAddress: String) {
        val apiService: ApiInterface =
            ApiClient.getKeyAuthenticationDetails().create(ApiInterface::class.java)
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
        val retrievedPrivateKeyFromDB = tokenRepo?.findAll() as List<TokenData>
        val privateKeyFromDB = retrievedPrivateKeyFromDB[0].privateKey
        return Base64.decode(privateKeyFromDB, Base64.DEFAULT)
    }

}

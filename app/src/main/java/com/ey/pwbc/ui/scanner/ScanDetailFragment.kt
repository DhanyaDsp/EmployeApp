package com.ey.pwbc.ui.scanner

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.conio.postequorum.SDK
import com.conio.postequorum.implementation.SDKFactory

import com.ey.pwbc.R
import com.ey.pwbc.Utils.Utils
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.databinding.ActivityLoginBinding
import com.ey.pwbc.databinding.ScanDetailFragmentBinding
import com.ey.pwbc.model.ScanData
import com.ey.pwbc.model.ScanStatus
import com.ey.pwbc.ui.authentication.LoginViewModel
import com.ey.pwbc.viewmodel.ScanDetailViewModel
import com.ey.pwbc.webservice.APICallback
import kotlinx.android.synthetic.main.scan_detail_fragment.view.*
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils
import org.web3j.tuples.generated.Tuple2
import java.math.BigInteger

class ScanDetailFragment : Fragment() {

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
        val dummyPrivateKey = byteArrayOf(
            26,
            1,
            -93,
            -125,
            -43,
            -76,
            -85,
            51,
            6,
            75,
            48,
            -100,
            9,
            20,
            -34,
            117,
            87,
            103,
            -111,
            -21,
            70,
            112,
            1,
            90,
            25,
            -3,
            15,
            125,
            110,
            -18,
            -92,
            0
        )
        val sdk = SDKFactory.getInstance().createSDK(dummyPrivateKey, Utils.getConf())
//            var productHash = sdk.computeProductHash(
//                "IPHONE 11",
//                "860",
//                BigInteger(str1, 16),
//                BigInteger(str2)
//            )
        var productHash = sdk.computeProductHash(
            "IPHONE 11",
            "096057bc9b0ba0406d050d0b6866fad8ac64a588",
            BigInteger("860"),
            BigInteger("1576882799")
        )
        val productHashHex = ByteUtils.toHexString(productHash)
        Log.d("sos", "productHashHex $productHashHex")

        val privateKeyAsString: String = android.util.Base64.encodeToString(
            dummyPrivateKey,
            android.util.Base64.DEFAULT
        )
        Log.d("sos", "dummyPrivateKey $privateKeyAsString")

        // api call
        buyVoucherInitAPI(productHashHex,dummyPrivateKey)
        sdk.dipendente().buyVoucher(productHash);
//        val tokenBalance = sdk.myTokenBalance()
//        Log.d("sos", "tokenBalance**:   $tokenBalance")
//        val voucherBalance = sdk.myVouchersBalance()
//        val employeeVoucherList = sdk.myVouchersList()
//        Log.d("sos", "voucherBalance**:   ${voucherBalance.component1().toString()}")
//        Log.d("sos", "employeeVoucherList**:   ${employeeVoucherList.get(0).toString()}")
//            BigInteger bi = new BigInteger(msg.getBytes());
//            System.out.println(new String (bi.toByteArray()));
    }

    private fun buyVoucherInitAPI(productHashHex: String?, dummyPrivateKey: ByteArray) {


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


}

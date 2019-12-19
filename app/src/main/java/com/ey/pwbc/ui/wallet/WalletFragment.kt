package com.ey.pwbc.ui.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.util.Log
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.conio.postequorum.implementation.SDKFactory
import com.ey.pwbc.R
import com.ey.pwbc.Utils.Utils
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.database.TokenData
import com.ey.pwbc.database.TokenRepo
import com.ey.pwbc.databinding.WalletFragmentBinding
import com.ey.pwbc.model.User
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.ui.scanner.ScanActivity
import com.ey.pwbc.webservice.APICallback
import com.ey.pwbc.webservice.ApiClient
import com.ey.pwbc.webservice.ApiInterface
import com.ey.pwbc.webservice.response.StoreKeyResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.wallet_fragment.view.*
import org.web3j.tuples.generated.Tuple2
import org.web3j.tuples.generated.Tuple3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger


class WalletFragment : Fragment() {
    private val SCAN: Int = 100;
    private lateinit var voucherRV: RecyclerView
    private lateinit var walletFragmentViewModel: WalletFragmentViewModel
    private var adapter: VoucherListAdapter? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var purchaseVoucherView: RelativeLayout
    private lateinit var voucherListTitle: TextView
    private lateinit var tokenBalanceView: TextView
    private lateinit var tokenValue: TextView
    private lateinit var refreshToken: TextView
    private lateinit var scanButton: FloatingActionButton
    private lateinit var zeroTokenView: ConstraintLayout
    private lateinit var binding: WalletFragmentBinding
    private lateinit var employeeName: TextView
    private var tokenRepo: TokenRepo? = null
    private var privateKey: ByteArray? = null

    val user = User

    companion object {
        @JvmStatic
        fun newInstance() =
            WalletFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.wallet_fragment, container, false)
        binding.lifecycleOwner = this
        val root = binding.root
        walletFragmentViewModel =
            ViewModelProviders.of(this).get(WalletFragmentViewModel::class.java)

        root.civScan.setOnClickListener {
            openScanner();
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseView(view)
        zeroBalanceTokenView()

        walletFragmentViewModel =
            ViewModelProviders.of(activity!!).get(WalletFragmentViewModel::class.java)
        walletFragmentViewModel.init()

        walletFragmentViewModel.getVoucher()?.observe(this, Observer {
            Log.d("sos", "Observer")
            adapter?.notifyDataSetChanged()
        })

        walletFragmentViewModel.getIsLoading()?.observe(this, Observer {
            if (it) showProgressBar()
            else hideProgressBar()
            voucherRV.smoothScrollToPosition(walletFragmentViewModel.getVoucher()?.value?.size!! - 1)

        })

//        tokenRepo = TokenRepo()
//        privateKey = getPrivateKeyFromDB()
        //fetchData()

    }

    private fun initialiseView(view: View?) {
        voucherRV = view!!.findViewById(R.id.voucher_List_rv)
        progressBar = view.findViewById(R.id.progressBar)
        purchaseVoucherView = view.findViewById(R.id.view_purchase_voucher)
        voucherListTitle = view.findViewById(R.id.txt_voucher_utilization)
        tokenBalanceView = view.findViewById(R.id.txt_token_balance)
        tokenValue = view.findViewById(R.id.txt_token_value)
        refreshToken = view.findViewById(R.id.txt_refresh_view)
        scanButton = view.findViewById(R.id.civScan)
        zeroTokenView = view.findViewById(R.id.view_no_token)
        employeeName = view.findViewById(R.id.txt_employee_name)

        //Retrieve from SharedPreference
        val preference= view.context.getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        val username= preference.getString("username","")
        val id= preference.getInt("id",0)
        employeeName.setText(username)

        voucherRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        voucherRV.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initRecyclerView() {
        val voucherList = ArrayList<Voucher>()
        voucherRV = view!!.findViewById(R.id.voucher_List_rv)
        voucherRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        voucherRV.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )
        val adapter = VoucherListAdapter(activity!!, voucherList)
        voucherRV.adapter = adapter
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun openScanner() {
        val intent = Intent(activity, ScanActivity::class.java)
        startActivityForResult(intent, SCAN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN && resultCode == 200) {
            val navController = activity?.findNavController(R.id.nav_host_fragment)
            val bundle = bundleOf("scanData" to data?.getSerializableExtra("scanData"))
            navController?.navigate(R.id.nav_scan, bundle)
        } else if (resultCode == -1) {
            Toast.makeText(activity, "Invalid QR Code", Toast.LENGTH_LONG).show()
        } else if (resultCode == 101) {
            //TODO: Launch wallet fragment
            Log.d("sos", "result from voucher")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun zeroBalanceTokenView() {
        voucherRV.visibility = View.GONE
        scanButton.visibility = View.GONE
        voucherListTitle.visibility = View.GONE

        tokenBalanceView.text = " WT 0"
        tokenValue.text = " WT 0"
        refreshToken.setOnClickListener {
            Toast.makeText(
                activity,
                "Please wait for some time to obtain balance",
                Toast.LENGTH_LONG
            ).show()
            PostWelfareAsync().execute()
        }

    }

    @SuppressLint("RestrictedApi")
    private fun refreshTokenBalance(): Tuple3<BigInteger, BigInteger, List<Voucher>> {
        // API call
        val tokenData = Utils.getKeyFromDB(activity)
        val privateKeyByteArray: ByteArray =
            android.util.Base64.decode(tokenData.component1(), android.util.Base64.DEFAULT)
        return displayBalance(privateKeyByteArray)
    }

    @SuppressLint("RestrictedApi")
    private fun displayBalance(privateKey: ByteArray): Tuple3<BigInteger, BigInteger, List<Voucher>> {
        val sdkEmployee = SDKFactory.getInstance().createSDK(privateKey, Utils.getConf())
        val employeeAddress = sdkEmployee.keyPair.noPrefixAddress
        val tokenBalance = sdkEmployee.myTokenBalance()
        val voucherBalance = sdkEmployee.myVouchersBalance().component1()
        val employeeVoucherList = sdkEmployee.myVouchersList()
        Log.d("sos", "voucher list: $employeeVoucherList")
        val voucherList = arrayListOf<Voucher>()
        for (employeeVoucher in employeeVoucherList) {
            val voucherDetails = sdkEmployee.voucherMetadata(employeeVoucher)
            Log.d("sos", "voucher1: ${voucherDetails.component1()}")
            Log.d("sos", "voucher2: ${voucherDetails.component2()}")
            Log.d("sos", "voucher3: ${voucherDetails.component3()}")
            Log.d("sos", "voucher4: ${voucherDetails.component4()}")
            Log.d("sos", "voucher5: ${voucherDetails.component5()}")
            Log.d("sos", "employeeVoucher: ${employeeVoucher}")

            voucherList.add(
                Voucher(
                    voucherDetails.component2(),
                    voucherDetails.component3().toString(),
                    voucherDetails.component1().toString(),
                    voucherDetails.component4().toString(),
                    employeeVoucher,
                    voucherDetails.component5().toString()
                )
            )
        }

        return Tuple3(tokenBalance, voucherBalance, voucherList)

    }

    inner class PostWelfareAsync :
        AsyncTask<Void, Void, Tuple3<BigInteger, BigInteger, List<Voucher>>>() {

        override fun doInBackground(vararg params: Void?): Tuple3<BigInteger, BigInteger, List<Voucher>> {
            try {
                return refreshTokenBalance()
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Display balance : " + e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("sos,", "AsyncTask exception:  ${e.localizedMessage}")

                Log.d("sos,", "AsyncTask exception:  ${e.toString()}")
            }
            return Tuple3(BigInteger.ZERO, BigInteger.ZERO, arrayListOf<Voucher>())
        }

        @SuppressLint("RestrictedApi")
        override fun onPostExecute(result: Tuple3<BigInteger, BigInteger, List<Voucher>>) {
            super.onPostExecute(result)
            // API call
            zeroTokenView.visibility = View.GONE
            tokenBalanceView.text = " WT " + result.component1()
            tokenValue.text = " WT " + result.component2()
            voucherRV.visibility = View.VISIBLE
            purchaseVoucherView.visibility = View.VISIBLE
            scanButton.visibility = View.VISIBLE
            voucherListTitle.visibility = View.VISIBLE

            voucherRV = view!!.findViewById(com.ey.pwbc.R.id.voucher_List_rv)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            voucherRV.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            voucherRV.setHasFixedSize(true)
            voucherRV.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )

            val adapter = VoucherListAdapter(activity!!, result.component3() as ArrayList<Voucher>)
            voucherRV.adapter = adapter
        }
    }
//    inner class BuyVoucherAsyn : AsyncTask<Void, Void, Tuple2<String, String>>() {
//
//        val sdkEmployee = SDKFactory.getInstance().createSDK(privateKey, Utils.getConf())
//        override fun doInBackground(vararg params: Void?): Tuple2<String, String> {
////            vgfdgdfg
//        }
//
//    }
}
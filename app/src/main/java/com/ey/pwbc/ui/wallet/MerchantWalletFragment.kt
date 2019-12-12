package com.ey.pwbc.ui.wallet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.model.User
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.ui.scanner.ScanActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.wallet_fragment.view.*


class MerchantWalletFragment : Fragment() {
    private val SCAN: Int = 100;
    private lateinit var voucherRV: RecyclerView
    private lateinit var storeName: TextView
    private lateinit var tokenValue: TextView
    private lateinit var walletFragmentViewModel: WalletFragmentViewModel
    private var adapter: VoucherListAdapter? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var refreshToken: TextView
    val user = User()

    companion object {
        @JvmStatic
        fun newInstance() =
            MerchantWalletFragment().apply {
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
        walletFragmentViewModel =
            ViewModelProviders.of(this).get(WalletFragmentViewModel::class.java)
        val root = inflater.inflate(R.layout.merchant_wallet_fragment, container, false)

        root.civScan.setOnClickListener {
            openScanner();
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseView(view)

        walletFragmentViewModel =
            ViewModelProviders.of(activity!!).get(WalletFragmentViewModel::class.java)
        walletFragmentViewModel.init()


        walletFragmentViewModel.getVoucher()?.observe(this, Observer {
            adapter?.notifyDataSetChanged()
        })

        walletFragmentViewModel.getIsLoading()?.observe(this, Observer {
            if (it) showProgressBar()
            else hideProgressBar()
            voucherRV.smoothScrollToPosition(walletFragmentViewModel.getVoucher()?.value?.size!! - 1)

        })

        fetchData()

    }

    private fun initialiseView(view: View?) {
        voucherRV = view!!.findViewById(R.id.voucher_List_rv)
        storeName = view.findViewById(R.id.txt_store_name)
        tokenValue = view.findViewById(R.id.txt_token_value)
        progressBar = view.findViewById(R.id.progressBar)
        refreshToken = view.findViewById(R.id.txt_refresh_view)

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
        val adapter = VoucherListAdapter(activity!!, voucherList, user)
        voucherRV.adapter = adapter
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun fetchData() {
        val voucherList = ArrayList<Voucher>()
        voucherList.add(Voucher("Borsone sportivo", "$30,00", com.ey.pwbc.R.drawable.ic_store, ""))
        voucherList.add(Voucher("Tuta", "$60,00", R.drawable.ic_store, ""))
        voucherList.add(Voucher("Borsone sportivo", "$20,00", R.drawable.ic_store, ""))

        voucherRV = view!!.findViewById(com.ey.pwbc.R.id.voucher_List_rv)
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

        val adapter = VoucherListAdapter(activity!!, voucherList, user)
        voucherRV.adapter = adapter
    }

    private fun openScanner() {
        val intent = Intent(activity, ScanActivity::class.java)
        startActivityForResult(intent, SCAN);
    }

}
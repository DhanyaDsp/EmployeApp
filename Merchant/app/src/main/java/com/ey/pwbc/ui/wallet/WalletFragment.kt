package com.ey.pwbc.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.model.User
import com.ey.pwbc.model.Voucher


class WalletFragment : Fragment() {

    private lateinit var voucherRV: RecyclerView
    private lateinit var merchantWalletFragmentViewModel: WalletFragmentViewModel
    private var adapter: VoucherListAdapter? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var purchaseVoucherView: RelativeLayout
    private lateinit var voucherListTitle: TextView
    private lateinit var tokenBalance: TextView
    private lateinit var tokenValue: TextView
    private lateinit var refreshToken: TextView
    val user = User()

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
        merchantWalletFragmentViewModel =
            ViewModelProviders.of(this).get(WalletFragmentViewModel::class.java)

        return inflater.inflate(R.layout.merchant_wallet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseView(view)

        merchantWalletFragmentViewModel =
            ViewModelProviders.of(activity!!).get(WalletFragmentViewModel::class.java)
        //merchantWalletFragmentViewModel.init()


        merchantWalletFragmentViewModel.getVoucher()?.observe(this, Observer {

            adapter?.notifyDataSetChanged()
        })

        merchantWalletFragmentViewModel.getIsLoading()?.observe(this, Observer {
            if (it) showProgressBar()
            else hideProgressBar()
            voucherRV.smoothScrollToPosition(merchantWalletFragmentViewModel.getVoucher()?.value?.size!! - 1)

        })

        fetchData()

    }

    private fun initialiseView(view: View?) {
        voucherRV = view!!.findViewById(R.id.voucher_List_rv)
        progressBar = view.findViewById(R.id.progressBar)
        purchaseVoucherView = view.findViewById(R.id.view_purchase_voucher)
        refreshToken = view.findViewById(R.id.txt_refresh_view)


        voucherRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        voucherRV.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )

    }


    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun fetchData() {
        val voucherList = ArrayList<Voucher>()
        voucherList.add(Voucher("Unieuro", "20WT", com.ey.pwbc.R.drawable.ic_camera))
        voucherList.add(Voucher("Adidas", "25WT", R.drawable.ic_shoe))
        voucherList.add(Voucher("Levis", "100WT", R.drawable.ic_shoe))
        voucherList.add(Voucher("Levis", "100WT", R.drawable.ic_shoe))
        voucherList.add(Voucher("Levis", "100WT", R.drawable.ic_shoe))

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

        val adapter = VoucherListAdapter(activity!!, voucherList)
        voucherRV.adapter = adapter
    }

}
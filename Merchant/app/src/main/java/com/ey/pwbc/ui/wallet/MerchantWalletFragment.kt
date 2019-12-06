package com.ey.pwbc.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.Utils
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.model.User
import com.ey.pwbc.model.Voucher
import kotlinx.android.synthetic.main.post_scan_fragment.view.*


class MerchantWalletFragment : Fragment() {
    private val SCAN: Int = 100;
    private lateinit var voucherRV: RecyclerView
    private lateinit var storeName: TextView
    private lateinit var transferVoucher: RelativeLayout
    private lateinit var tokenValue: TextView
    private lateinit var merchantWalletFragmentViewModel: WalletFragmentViewModel
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
        merchantWalletFragmentViewModel =
            ViewModelProviders.of(this).get(WalletFragmentViewModel::class.java)


        return inflater.inflate(R.layout.merchant_wallet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseView(view)
        // initBinding()
        onTransferVoucherClick()

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
        storeName = view.findViewById(R.id.txt_store_name)
        tokenValue = view.findViewById(R.id.txt_token_value)
        progressBar = view.findViewById(R.id.progressBar)
        refreshToken = view.findViewById(R.id.txt_refresh_view)
        transferVoucher = view.findViewById(R.id.view_purchase_voucher)

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

    private fun fetchData() {
        val voucherList = ArrayList<Voucher>()
        voucherList.add(Voucher("Borsone sportivo", "€ 30,00", com.ey.pwbc.R.drawable.ic_store))
        voucherList.add(Voucher("Tuta", "€ 60,00", R.drawable.ic_store))
        voucherList.add(Voucher("Borsone sportivo", "€ 20,00", R.drawable.ic_store))

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

        val adapter = VoucherListAdapter(activity!!, voucherList)
        voucherRV.adapter = adapter
    }


    fun onTransferVoucherClick() {
        merchantWalletFragmentViewModel.onTransferVoucherClick()


        transferVoucher.setOnClickListener(View.OnClickListener {
            Utils.showAlert(
                "Tutti i voucher da te accumulati finora verranno trasferito a Poste Italiane",
                "Confermi il trasferimento?",
                activity!!,
                View.OnClickListener { moveToPostRedeemVoucher() },
                View.OnClickListener { },
                View.OnClickListener { },
                Utils.TYPE_CONFIRM
            )
        })


    }

    private fun moveToPostRedeemVoucher() {
        val redeemVoucherView =
            activity!!.findViewById<ConstraintLayout>(com.ey.pwbc.R.id.view_merchant_post_redeem_voucher)
        val closeImageView = activity!!.findViewById<ImageView>(com.ey.pwbc.R.id.ivClose)


        redeemVoucherView.visibility = View.VISIBLE
        redeemVoucherView.tvMsg1.text =
            "Tutti i voucher da te accumulati finora verranno trasferito a Poste Italiane"
        redeemVoucherView.tvMsg2.text = "A breve riceverai il bonifico all'lBAN IT1234567787"

        closeImageView.setOnClickListener {
            //  hideToolbar()
            redeemVoucherView.visibility = View.GONE
        }
    }
}


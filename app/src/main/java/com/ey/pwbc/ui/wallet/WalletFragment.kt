package com.ey.pwbc.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.ui.scanner.ScanActivity
import kotlinx.android.synthetic.main.wallet_fragment.view.*


class WalletFragment : Fragment() {
    private val SCAN: Int = 100;
    private lateinit var voucherRV: RecyclerView
    private lateinit var walletFragmentViewModel: WalletFragmentViewModel
    private var adapter: VoucherListAdapter? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager

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
        walletFragmentViewModel =
            ViewModelProviders.of(this).get(WalletFragmentViewModel::class.java)
        val root = inflater.inflate(R.layout.wallet_fragment, container, false)
        /*       walletFragmentViewModel.getVoucher()?.observe(this, Observer {
                   Log.d("sos", "Observer 11")
                   adapter.notifyDataSetChanged()

               })*/
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
            Log.d("sos", "Observer")
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
        progressBar = view.findViewById(R.id.progressBar)
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

    public fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    public fun hideProgressBar() {
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
        //layoutManager.scrollToPositionWithOffset(2, 20);
        voucherRV.isNestedScrollingEnabled = false;
        voucherRV.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
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
        }
    }
}
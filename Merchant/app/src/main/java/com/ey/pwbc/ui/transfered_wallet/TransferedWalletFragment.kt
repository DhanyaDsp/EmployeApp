package com.ey.pwbc.ui.transfered_wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.adapters.VoucherListAdapter
import com.ey.pwbc.model.Voucher
import com.google.android.material.textfield.TextInputLayout

class TransferedWalletFragment : Fragment() {


    private lateinit var voucherRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        profileViewModel =
//            ViewModelProviders.of(this).get(EmployeeProfileViewModel::class.java)
//                profileViewModel.text.observe(this, Observer {
//
//        })


        return inflater.inflate(R.layout.issued_wallet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchData()
    }
    private fun fetchData() {
        val voucherList = ArrayList<Voucher>()
        voucherList.add(Voucher("Borsone sportivo", "€ 930,00", R.drawable.ic_store))
        voucherList.add(Voucher("Tuta", "€ 980,00", R.drawable.ic_store))
        voucherList.add(Voucher("Borsone sportivo", "€ 920,00", R.drawable.ic_store))

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

}
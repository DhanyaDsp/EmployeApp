package com.ey.pwbc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.repositories.VoucherRepository


class WalletFragmentViewModel : ViewModel() {

    private val voucherList: MutableLiveData<List<Voucher>>? = null
    private lateinit var mRepo: VoucherRepository

    public fun init() {
        if (voucherList != null) {
            return
        }
        mRepo = VoucherRepository.instance
    }

    fun getVoucher(): LiveData<List<Voucher>>? {
        return voucherList
    }
}
package com.ey.pwbc.ui.wallet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.repositories.VoucherRepository


class WalletFragmentViewModel : ViewModel() {

    private var voucherList: MutableLiveData<MutableList<Voucher>>?  = MutableLiveData()
    private var isUpdating: MutableLiveData<Boolean>? = null
    private lateinit var mRepo: VoucherRepository

    public fun init() {
        if (voucherList != null) {
            return
        }
        mRepo = VoucherRepository.instance

    }

    fun getVoucher(): MutableLiveData<MutableList<Voucher>>? {
        mRepo = VoucherRepository.instance
        Log.d("sos","voucher list *: "+mRepo.getVoucher())
        //voucherList?.postValue(mRepo.getVoucher())
        return voucherList
    }

    //we can call this method when user refresh button clicks
    fun addValue(voucher: Voucher) {
        isUpdating?.value = true;
        //call a service-> inside the service success method:
        var currentVoucher: MutableList<Voucher>? = voucherList?.value
        currentVoucher?.add(voucher)
        voucherList?.postValue(currentVoucher)
        isUpdating?.postValue(false)

    }

    fun getIsLoading(): MutableLiveData<Boolean>? {
        return isUpdating
    }
}
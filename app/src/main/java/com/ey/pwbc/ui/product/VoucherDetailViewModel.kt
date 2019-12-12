package com.ey.pwbc.ui.product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ey.pwbc.model.VoucherDetail

class VoucherDetailViewModel : ViewModel() {
    private var voucherDetail = MutableLiveData<VoucherDetail>()

    fun getVoucherDetail(): MutableLiveData<VoucherDetail> {
        return voucherDetail
    }

    fun onVoucherClick(voucher: VoucherDetail) {
        this.voucherDetail.value = voucher
    }

    fun onVoucherClick1() {
        Log.d("sos", "voucher view model")
    }
}
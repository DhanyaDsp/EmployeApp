package com.ey.pwbc.repositories

import androidx.lifecycle.MutableLiveData
import com.ey.pwbc.model.Voucher

object VoucherRepository {
    init {
        println("Singleton class invoked.")
    }

    private var voucherRepository: VoucherRepository? = null
    var voucherList: ArrayList<Voucher>? = null


    val instance: VoucherRepository
        get() {
            if (voucherRepository == null) voucherRepository = VoucherRepository
            return voucherRepository!!
        }

    public fun getVoucher(): MutableLiveData<MutableList<Voucher>> {

        val data = MutableLiveData<MutableList<Voucher>>()
        data.value = voucherList
        return data
    }


}
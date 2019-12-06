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
        setVoucher()
        val data = MutableLiveData<MutableList<Voucher>>()
        data.value = voucherList
        return data
    }

    private fun setVoucher() {
        voucherList?.add(Voucher("Adidas", "100 WT", 1,""))
        voucherList?.add(Voucher("Levis", "10 QT", 1,""))
        voucherList?.add(Voucher("Reymond", "50 QT", 3,""))
        voucherList?.add(Voucher("Puma", "70 QT", 2,""))


    }
}
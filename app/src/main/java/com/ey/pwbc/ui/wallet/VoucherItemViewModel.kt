package com.ey.pwbc.ui.wallet

import android.text.TextUtils
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ey.pwbc.model.Voucher

class VoucherItemViewModel(private val voucher: Voucher) : BaseObservable() {
    var voucherModel: Voucher? = null

    init {
        this.voucherModel = voucher
    }

    fun setUp() {

    }

    fun tearDown() {

    }


}
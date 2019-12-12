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

    @Bindable
    fun getVoucherName(): String {
        return if (!TextUtils.isEmpty(voucherModel?.voucherName)) voucherModel!!.voucherName else ""
    }

    @Bindable
    fun getVoucherAmount(): String {
        return if (!TextUtils.isEmpty(voucherModel?.voucherAmount)) voucherModel!!.voucherAmount else ""
    }

    @Bindable
    fun getStoreName(): String {
        return if (!TextUtils.isEmpty(voucherModel?.storeName)) voucherModel!!.storeName else ""
    }
}
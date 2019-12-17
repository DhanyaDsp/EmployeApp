package com.ey.pwbc.ui.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ey.pwbc.R
import com.ey.pwbc.UtilsDialog
import com.ey.pwbc.databinding.ActivityVoucherDetailBinding
import com.ey.pwbc.model.ScanData
import com.ey.pwbc.model.User
import com.ey.pwbc.ui.dashboard.LandingActivity
import kotlinx.android.synthetic.main.app_bar_landing.*
import kotlinx.android.synthetic.main.post_scan_fragment.view.*


class VoucherDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: VoucherDetailViewModel
    private lateinit var binding: ActivityVoucherDetailBinding
    private var deepLinkData: ScanData? = null
    var toolbar: Toolbar? = null;
    val user = User
    private var et_name: EditText? = null
    private var et_value: EditText? = null
    private var et_merchant: EditText? = null
    private var et_deadline: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        et_name = findViewById(R.id.et_name)
        et_value = findViewById(R.id.et_value)
        et_merchant = findViewById(R.id.et_merchant)
        et_deadline = findViewById(R.id.et_deadline)

        if (intent != null) {
            val intent = intent
            val prod = intent.data.toString()
            Log.d("sos", "data:  ${intent.data}")
            val array = prod.split(",")
            if (intent.data == null) {
                val scanData = ScanData("", "20 WT", "Adidas Store", "21/12/2019")

                deepLinkData = scanData

                et_name?.setText(scanData.name)
                et_value?.setText(scanData.value)
                et_merchant?.setText(scanData.merchant)
                et_deadline?.setText(scanData.date)
            } else {
                val scanData = ScanData(array[0], array[1], array[2], array[3])

                deepLinkData = scanData

                Log.e("###sos","deeplinking  name : "+scanData.name)
                Log.e("###sos","deeplinking  value : "+scanData.value)
                Log.e("###sos","deeplinking  merchant : "+scanData.merchant)
                Log.e("###sos","deeplinking  date : "+scanData.date)

                et_name?.setText(scanData.name)
                et_value?.setText(scanData.value)
                et_merchant?.setText(scanData.merchant)
                et_deadline?.setText(scanData.date)
            }

            if (array.size == 4) {

            }
        }
        initBinding()
        initToolbar()

    }


    private fun initToolbar() {
        toolbar = findViewById(com.ey.pwbc.R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        tvTitle.text = getString(com.ey.pwbc.R.string.voucher_title)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, com.ey.pwbc.R.layout.activity_voucher_detail)
        binding.lifecycleOwner = this
        binding.deepLinkData = deepLinkData;
        viewModel = ViewModelProviders.of(this).get(VoucherDetailViewModel::class.java)
        binding.voucherDetailViewModel = viewModel
        //if we need to update the voucher fields from the edittext then we can add a voucher model here and bind it with layout.

    }

    fun hideToolbar() {
        toolbar?.visibility = View.GONE
    }

    fun onVoucherRedeemClick(view: View) {
        viewModel.onVoucherClick1()

        UtilsDialog.showAlert(
            "ll  tuo Voucher verra trasferito ad Adidas Store",
            "Confermi?",
            this,
            View.OnClickListener { moveToPostRedeemVoucher() },
            View.OnClickListener { },
            View.OnClickListener { },
            UtilsDialog.TYPE_CONFIRM
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.ey.pwbc.R.menu.landing, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.ey.pwbc.R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveToPostRedeemVoucher() {
        val redeemVoucherView =
            this.findViewById<ConstraintLayout>(com.ey.pwbc.R.id.view_post_redeem_voucher)
        val closeImageView = this.findViewById<ImageView>(com.ey.pwbc.R.id.ivClose)



        redeemVoucherView.visibility = View.VISIBLE
        closeImageView.setOnClickListener {
            //  hideToolbar()
            intent = Intent(this, LandingActivity::class.java)
            startActivityForResult(intent, 101)
            //startActivity(intent)
            //redeemVoucherView.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
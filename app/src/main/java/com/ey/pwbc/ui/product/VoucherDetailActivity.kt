package com.ey.pwbc.ui.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
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
import com.ey.pwbc.model.User
import com.ey.pwbc.ui.dashboard.LandingActivity
import kotlinx.android.synthetic.main.app_bar_landing.*
import kotlinx.android.synthetic.main.post_scan_fragment.view.*


class VoucherDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: VoucherDetailViewModel
    private lateinit var binding: ActivityVoucherDetailBinding
    var toolbar: Toolbar? = null;
    val user = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initBinding()
        initToolbar()
        val intent  = intent
        val prod = intent.data.toString()
        Log.d("os","data $prod")

        if (user.getUserType() == User.TYPE_MERCHANT) {
            removeEmployeeField()
        }
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

        viewModel = ViewModelProviders.of(this).get(VoucherDetailViewModel::class.java)
        binding.voucherDetailViewModel = viewModel
        //if we need to update the voucher fields from the edittext then we can add a voucher model here and bind it with layout.

    }

    fun hideToolbar() {
        toolbar?.visibility = View.GONE
    }

    fun onVoucherRedeemClick(view: View) {
        viewModel.onVoucherClick1()
        if (user.getUserType() == User.TYPE_EMPLOYEE) {
            UtilsDialog.showAlert(
                "ll  tuo Voucher verra trasferito ad Adidas Store",
                "Confermi?",
                this,
                View.OnClickListener { moveToPostRedeemVoucher() },
                View.OnClickListener { },
                View.OnClickListener { },
                UtilsDialog.TYPE_CONFIRM
            )
        } else {
            UtilsDialog.showAlert(
                "ll  tuo Voucher verra trasferito a Poste Italiane",
                "Confermi il trasferimento?",
                this,
                View.OnClickListener { moveToPostRedeemVoucher() },
                View.OnClickListener { },
                View.OnClickListener { },
                UtilsDialog.TYPE_CONFIRM
            )
        }
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

        if (user.getUserType() == User.TYPE_EMPLOYEE) {

            redeemVoucherView.visibility = View.VISIBLE
            closeImageView.setOnClickListener {
                //  hideToolbar()
                intent = Intent(this, LandingActivity::class.java)
                startActivityForResult(intent, 101)
                //startActivity(intent)
                //redeemVoucherView.visibility = View.GONE
            }

        } else if (user.getUserType() == User.TYPE_MERCHANT) {
            redeemVoucherView.visibility = View.VISIBLE
            redeemVoucherView.tvMsg1.text = "II tuo voucher e stato trasferito a Poste Italiane!"
            redeemVoucherView.tvMsg2.text = "A breve riceverai il bonifico all'lBAN IT1234567787"

            closeImageView.setOnClickListener {
                //  hideToolbar()
                redeemVoucherView.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun removeEmployeeField() {
        val performedBy = this.findViewById<RelativeLayout>(R.id.rl_performed_by)
        val voucherTransferButton = this.findViewById<Button>(R.id.btn_use_voucher)
        performedBy.visibility = View.GONE
        voucherTransferButton.text = "TRASFERISCI VOUCHER";
    }
}
package com.ey.pwbc.ui.product

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.ey.pwbc.R
import com.ey.pwbc.model.ScanStatus
import com.ey.pwbc.viewmodel.PostScanViewModel
import kotlinx.android.synthetic.main.post_scan_fragment.view.*

class VoucherDetailPromptFragment : Fragment() {

    private var voucherRedeemStatus: ScanStatus? = null;
    private lateinit var viewModel: RedeemVoucherViewModel

    companion object {
        @JvmStatic
        fun newInstance(scan: ScanStatus) = VoucherDetailPromptFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.post_scan_fragment, container, false)
        root.ivClose.setOnClickListener {
            val navController = activity?.findNavController(R.id.nav_host_fragment)
            navController?.popBackStack()
        }
        voucherRedeemStatus?.let {
            if (voucherRedeemStatus?.status == ScanStatus.STATUS_SUCCESS) {
                root.ivStatus.setImageResource(R.drawable.ic_checked)
                root.tvStatus.text = resources.getString(R.string.transaction_success)
                root.tvMsg1.text = "II Voucher e stato correttamente trasferito"
                root.tvMsg2.text =
                    ""
            } else {
                root.ivStatus.setImageResource(R.drawable.ic_close)
                root.tvStatus.text = resources.getString(R.string.transaction_failed)
                root.tvMsg1.text = "Impossibile acquistare il voucher."
                root.tvMsg2.text = "<<Disponibilita di Welfare Token non sufficiente>>"
            }
        }
        return root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_post_scan, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.close -> {
                val navController = activity?.findNavController(R.id.nav_host_fragment)
                navController?.popBackStack()
                return true;
            }
        }
        return false;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RedeemVoucherViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getSerializable("scanStatus")?.let {
            this.voucherRedeemStatus = it as ScanStatus;
        }
    }

}

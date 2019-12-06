package com.ey.pwbc.ui.scanner

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.ey.pwbc.R
import com.ey.pwbc.databinding.ActivityLoginBinding
import com.ey.pwbc.databinding.ScanDetailFragmentBinding
import com.ey.pwbc.model.ScanData
import com.ey.pwbc.model.ScanStatus
import com.ey.pwbc.ui.authentication.LoginViewModel
import com.ey.pwbc.viewmodel.ScanDetailViewModel
import kotlinx.android.synthetic.main.scan_detail_fragment.view.*

class ScanDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ScanDetailFragment()
        @JvmStatic
        fun newInstance(scanData: ScanData) = ScanDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("scanData", scanData)
            }
        }
    }

    private var scanData: ScanData? = null
    private lateinit var viewModel: ScanDetailViewModel
    private lateinit var binding: ScanDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.scan_detail_fragment, container, false)
        binding.lifecycleOwner = this
        binding.scanData = scanData;
        return binding.root;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getSerializable("scanData")?.let {
            this.scanData = it as ScanData;
        }
    }
}

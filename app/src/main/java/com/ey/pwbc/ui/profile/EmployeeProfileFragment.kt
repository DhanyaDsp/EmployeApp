package com.ey.pwbc.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ey.pwbc.R
import com.google.android.material.textfield.TextInputLayout

class EmployeeProfileFragment : Fragment() {

    private lateinit var profileViewModel: EmployeeProfileViewModel
    private var nameInputLayout: TextInputLayout? = null
    private var emailInputLayout: TextInputLayout? = null
    private var nameInputEditText: TextInputLayout? = null
    private var emailInputEditText: TextInputLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialiseView(view)
        profileViewModel =
            ViewModelProviders.of(this).get(EmployeeProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile_employee, container, false)
        profileViewModel.text.observe(this, Observer {

        })
        return root
    }

    private fun initialiseView(view: View?) {
        nameInputLayout = view?.findViewById(R.id.txt_input_layout_employee_profile_name);
        emailInputLayout = view?.findViewById(R.id.txt_input_layout_employee_profile_email);
        nameInputEditText = view?.findViewById(R.id.edt_employee_profile_name);
        emailInputEditText = view?.findViewById(R.id.edt_employee_profile_email);

        nameInputLayout?.setHint("Name");
        emailInputLayout?.hint = "E-mail";
        nameInputEditText?.isEnabled = false;
        emailInputEditText?.isEnabled = false;

    }
}
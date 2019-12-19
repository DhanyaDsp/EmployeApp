package com.ey.pwbc.ui.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class LogoutFragment : Fragment() {

    private lateinit var toolsViewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolsViewModel =
            ViewModelProviders.of(this).get(LogoutViewModel::class.java)
        val root = inflater.inflate(com.ey.pwbc.R.layout.fragment_logout, container, false)
        logoutAlertDialog()
        toolsViewModel.text.observe(this, Observer {
        })
        return root
    }

    private fun logoutAlertDialog() {

        val dialogBuilder = AlertDialog.Builder(activity!!)

        dialogBuilder.setMessage("Sei sicuro di voler effettuare il logout ?")
            .setCancelable(false)
            .setPositiveButton("PROCEDI") { _, _ ->
                activity!!.finish()
            }
            .setNegativeButton("ANNULLA") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Logout")
        alert.show()

    }
}
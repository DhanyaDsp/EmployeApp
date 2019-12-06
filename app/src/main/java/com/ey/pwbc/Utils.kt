package com.ey.pwbc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView


class Utils {
    companion object {

        val TYPE_ALERT = 1;
        val TYPE_CONFIRM = 2;

        fun showAlert(
            msg: String,
            confirmMsg: String,
            context: Context,
            confirmListener: View.OnClickListener,
            okListener: View.OnClickListener,
            cancelListener: View.OnClickListener,
            type: Int
        ) {
            val dialog = AppCompatDialog(context);
            val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
            dialog.setContentView(dialogView)
            val okBtn = dialogView.findViewById<AppCompatButton>(R.id.btnOK);
            val confirmBtn = dialogView.findViewById<AppCompatButton>(R.id.btnConfirm);
            val cancelBtn = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
            val tvMessage = dialogView.findViewById<AppCompatTextView>(R.id.tvMessage)
            val tvConfirmMsg = dialogView.findViewById<AppCompatTextView>(R.id.tvConfirmMsg)
            okBtn.setOnClickListener {
                dialog.dismiss()
                okListener.onClick(it)
            }
            confirmBtn.setOnClickListener {
                dialog.dismiss()
                confirmListener.onClick(it)
            }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
                cancelListener.onClick(it)
            }
            tvMessage.text = msg
            tvConfirmMsg.text = confirmMsg
            if (type == TYPE_ALERT) {
                okBtn.visibility = View.VISIBLE
                cancelBtn.visibility = View.GONE
                confirmBtn.visibility = View.GONE
                tvConfirmMsg.visibility = View.GONE
            } else {
                okBtn.visibility = View.GONE
                cancelBtn.visibility = View.GONE
                confirmBtn.visibility = View.VISIBLE
                cancelBtn.visibility = View.VISIBLE
                tvConfirmMsg.visibility = View.VISIBLE
            }
            dialog.show()
        }
    }
}
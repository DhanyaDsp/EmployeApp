package com.ey.pwbc.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ey.pwbc.R
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.ui.product.VoucherDetailActivity
import de.hdodenhof.circleimageview.CircleImageView

class VoucherListAdapter(
    private val context: Context,
    private val voucherList: ArrayList<Voucher>
) :
    RecyclerView.Adapter<VoucherListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.voucher_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(voucherList[position])
    }

    override fun getItemCount(): Int {
        Log.d("sos", "size: " + voucherList.size)
        return voucherList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(voucher: Voucher) {
            val voucherNameTV = itemView.findViewById(R.id.txt_voucher_name) as TextView
            val voucherAmountTV = itemView.findViewById(R.id.txt_voucher_amount) as TextView
//            val voucherImageIV = itemView.findViewById(R.id.img_voucher_image) as CircleImageView
            val voucherImageIV = itemView.findViewById(R.id.img_voucher_image) as ImageView
            voucherNameTV.text = voucher.voucherName
            voucherAmountTV.text = voucher.voucherAmount
            voucherImageIV.setImageResource(voucher.voucherImage)
            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(itemView.context, VoucherDetailActivity::class.java)
                itemView.context.startActivity(intent)
            })
        }
    }

}
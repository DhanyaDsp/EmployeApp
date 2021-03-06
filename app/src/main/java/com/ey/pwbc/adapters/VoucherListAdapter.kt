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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ey.pwbc.R
import com.ey.pwbc.model.User
import com.ey.pwbc.model.Voucher
import com.ey.pwbc.ui.product.VoucherDetailActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.voucher_list_item.view.*

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
        holder.bindItems(voucherList[position],position)
    }

    override fun getItemCount(): Int {
        Log.d("sos", "size: " + voucherList.size)
        return voucherList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(voucher: Voucher,position: Int) {
            val voucherNameTV = itemView.findViewById(R.id.txt_voucher_name) as TextView
           // val voucherAmountTV = itemView.findViewById(R.id.txt_voucher_amount) as TextView
            val voucherImageIV = itemView.findViewById(R.id.img_voucher_image) as ImageView
            val storeName = itemView.findViewById(R.id.txt_store_name) as TextView
            voucherNameTV.text = voucher.name
           // voucherAmountTV.text = voucher.value
            //store name is voucher value: need to give proper mapping
            storeName.text = "-WT "+voucher.merchant

            Glide.with(itemView.context)
                .load(R.drawable.ic_store)
                .apply(RequestOptions.circleCropTransform())
                .into(voucherImageIV)

            itemView.setOnClickListener(View.OnClickListener {


                val intent = Intent(itemView.context, VoucherDetailActivity::class.java)
                intent.putExtra("voucher_name",voucher.name)
                intent.putExtra("voucher_value",voucher.value)
                intent.putExtra("merchant",voucher.merchant)
                intent.putExtra("voucher_date",voucher.date)
                intent.putExtra("voucher_id", voucher.voucherId)
                intent.putExtra("merchant_address", voucher.merchantAddress)
                intent.putExtra("position",position)
                itemView.context.startActivity(intent)
            })
        }


    }

}
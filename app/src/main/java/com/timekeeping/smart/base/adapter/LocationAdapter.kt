package com.timekeeping.smart.base.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timekeeping.smart.R
import com.timekeeping.smart.entity.response.LocationResponse
import com.timekeeping.smart.extension.inflate
import kotlinx.android.synthetic.main.item_add_address.view.tv_id_location
import kotlinx.android.synthetic.main.item_add_address.view.tv_latitude
import kotlinx.android.synthetic.main.item_add_address.view.tv_longitude
import kotlinx.android.synthetic.main.item_add_address.view.tv_name_location
import kotlinx.android.synthetic.main.item_add_address.view.tv_note
import kotlinx.android.synthetic.main.item_add_address.view.tv_stt
import javax.inject.Inject

class LocationAdapter @Inject constructor(context: Context) :
    EndlessLoadingRecyclerViewAdapter(context, false) {
    override fun initNormalViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        return NormalViewHolder(parent.inflate(R.layout.item_add_address))
    }

    override fun bindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position, LocationResponse::class.java)
        item?.let {
            holder.itemView.apply {
                val stt = position + 1
                tv_stt.text = stt.toString()
                tv_name_location.text = it.TENVT
                tv_id_location.text = it.MAVT
                tv_longitude.text = it.KINHDO?.toString() ?: ""
                tv_latitude.text = it.VIDO?.toString() ?: ""
                tv_note.text = it.GHICHU
            }
        }
    }
}
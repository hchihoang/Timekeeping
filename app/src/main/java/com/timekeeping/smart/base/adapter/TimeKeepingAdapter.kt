package com.timekeeping.smart.base.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timekeeping.smart.R
import com.timekeeping.smart.entity.response.LocationResponse
import com.timekeeping.smart.entity.response.TimeKeepingResponse
import com.timekeeping.smart.extension.dateTimeToDate
import com.timekeeping.smart.extension.dateTimeToTime
import com.timekeeping.smart.extension.inflate
import kotlinx.android.synthetic.main.item_add_address.view.tv_stt
import kotlinx.android.synthetic.main.item_time_keeping.view.tv_date
import kotlinx.android.synthetic.main.item_time_keeping.view.tv_time
import javax.inject.Inject

class TimeKeepingAdapter @Inject constructor(context: Context) :
    EndlessLoadingRecyclerViewAdapter(context, false) {
    override fun initNormalViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        return NormalViewHolder(parent.inflate(R.layout.item_time_keeping))
    }

    override fun bindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position, TimeKeepingResponse::class.java)
        item?.let {
            holder.itemView.apply {
                val stt = position + 1
                tv_stt.text = stt.toString()
                tv_date.text = it.TRX_DATE?.dateTimeToDate()
                tv_time.text = it.GIOCHAM?.dateTimeToTime()
            }
        }
    }
}
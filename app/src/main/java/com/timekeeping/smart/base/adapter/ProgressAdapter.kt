package com.timekeeping.smart.base.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timekeeping.smart.R
import com.timekeeping.smart.entity.response.ProgressResponse
import com.timekeeping.smart.extension.inflate
import javax.inject.Inject

class ProgressAdapter @Inject constructor(context: Context) :
    EndlessLoadingRecyclerViewAdapter(context, false) {
    override fun initNormalViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        return NormalViewHolder(parent.inflate(R.layout.item_add_address))
    }

    override fun bindNormalViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position, ProgressResponse::class.java)
        item?.let {
            holder.itemView.apply {

            }
        }
    }
}
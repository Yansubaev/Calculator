package com.yans.calculator.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yans.calculator.data.HistoryEventData
import com.yans.calculator.databinding.ItemHistoryEventBinding

class HistoryRecyclerViewAdapter(
    private val context: Context
) : ListAdapter<HistoryEventData, HistoryEventViewHolder>(DiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryEventViewHolder =
        HistoryEventViewHolder(
            ItemHistoryEventBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HistoryEventViewHolder, position: Int) {
        holder.initView(getItem(position))
    }
}

class DiffUtilsCallback : DiffUtil.ItemCallback<HistoryEventData>() {
    override fun areItemsTheSame(oldItem: HistoryEventData, newItem: HistoryEventData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: HistoryEventData, newItem: HistoryEventData): Boolean =
        oldItem.contentEquals(newItem)
}
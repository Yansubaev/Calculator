package com.yans.calculator.recycler

import androidx.recyclerview.widget.RecyclerView
import com.yans.calculator.data.HistoryEventData
import com.yans.calculator.databinding.ItemHistoryEventBinding

class HistoryEventViewHolder(private val binding: ItemHistoryEventBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun initView(model: HistoryEventData){
        binding.txtInput.text = model.input
        binding.txtResult.text = model.result
    }
}
package com.example.flo_clone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemChartBinding
import com.example.flo_clone.model.ChartItem

class ChartAdapter(private val itemList: List<ChartItem>) :
    RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {

    inner class ChartViewHolder(private val binding: ItemChartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChartItem) {
            binding.chartItemTitleTv.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ItemChartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}

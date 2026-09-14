package com.example.s8128637assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s8128637assignment2.databinding.ItemEntityBinding
import com.example.s8128637assignment2.util.EntityUiMapper

class EntityAdapter(
    private val onItemClick: (Map<String, Any>) -> Unit
) : ListAdapter<Map<String, Any>, EntityAdapter.EntityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EntityViewHolder(private val binding: ItemEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Map<String, Any>) {
            binding.textTitle.text = EntityUiMapper.title(entity)
            val subtitle = EntityUiMapper.subtitle(entity)
            binding.textSubtitle.text = subtitle
            binding.textSubtitle.visibility = if (subtitle.isNotBlank()) View.VISIBLE else View.GONE
            binding.itemContent.setOnClickListener { onItemClick(entity) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Map<String, Any>>() {
        override fun areItemsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Map<String, Any>, newItem: Map<String, Any>) =
            oldItem == newItem
    }
}

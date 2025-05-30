package com.example.fooddeliveryapp.utils

import androidx.appcompat.widget.SearchView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.databinding.ItemRestaurantBinding
import com.example.fooddeliveryapp.entities.model.ProductEntity

class RestaurantAdapter(
    private val onClick: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, RestaurantAdapter.ViewHolder>(DiffCallback()), Filterable {

    private var fullList: List<ProductEntity> = emptyList()

    override fun submitList(list: List<ProductEntity>?) {
        super.submitList(list)
        fullList = list ?: emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?) = FilterResults().apply {
            val query = constraint?.toString()?.lowercase()?.trim() ?: ""
            values = if (query.isEmpty()) fullList else fullList.filter {
                it.name.lowercase().contains(query) || it.category.lowercase().contains(query)
            }
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            submitList(results?.values as? List<ProductEntity>)
        }
    }

    inner class ViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            binding.tvName.text = item.name
            binding.tvCategory.text = item.category
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(old: ProductEntity, new: ProductEntity) = old.id == new.id
        override fun areContentsTheSame(old: ProductEntity, new: ProductEntity) = old == new
    }
}
package com.example.fooddeliveryapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.ItemRestaurantBinding
import com.example.fooddeliveryapp.entities.model.RestaurantEntity

class RestaurantAdapter(
    private val onClick: (RestaurantEntity) -> Unit
) : ListAdapter<RestaurantEntity, RestaurantAdapter.ViewHolder>(DiffCallback()), Filterable {

    private var fullList: List<RestaurantEntity> = emptyList()

    override fun submitList(list: List<RestaurantEntity>?) {
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
            submitList(results?.values as? List<RestaurantEntity>)
        }
    }

    inner class ViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: RestaurantEntity) {
            binding.tvName.text = item.name
            binding.tvCategory.text = item.category

            Glide.with(binding.ivRestaurantImage.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivRestaurantImage)

            binding.root.setOnClickListener { onClick(item) }
            binding.root.setOnClickListener {
                Log.d("RestaurantAdapter", "Clicked restaurant in adapter: ${item.name}")
                onClick(item)
            }

        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<RestaurantEntity>() {
        override fun areItemsTheSame(old: RestaurantEntity, new: RestaurantEntity) = old.id == new.id
        override fun areContentsTheSame(old: RestaurantEntity, new: RestaurantEntity) = old == new
    }
}

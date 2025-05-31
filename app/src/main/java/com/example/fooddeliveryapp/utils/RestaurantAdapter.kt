package com.example.fooddeliveryapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.ItemRestaurantBinding
import com.example.fooddeliveryapp.entities.model.RestaurantEntity
class RestaurantAdapter(
    private val onClick: (RestaurantEntity) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>(), Filterable {

    private var fullList: List<RestaurantEntity> = emptyList()
    private var filteredList: List<RestaurantEntity> = emptyList()

    fun submitList(list: List<RestaurantEntity>) {
        fullList = list
        filteredList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint?.toString()?.lowercase()?.trim() ?: ""
            val results = if (query.isEmpty()) {
                fullList
            } else {
                fullList.filter {
                    it.name.lowercase().contains(query) || it.category.lowercase().contains(query)
                }
            }
            return FilterResults().apply { values = results }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            filteredList = results?.values as? List<RestaurantEntity> ?: emptyList()
            notifyDataSetChanged()
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

            binding.root.setOnClickListener {
                Log.d("RestaurantAdapter", "Clicked restaurant: ${item.name}")
                onClick(item)
            }
        }
    }
}
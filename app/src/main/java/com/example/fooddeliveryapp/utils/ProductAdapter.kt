package com.example.fooddeliveryapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddeliveryapp.databinding.ItemProductBinding
import com.example.fooddeliveryapp.entities.model.ProductEntity

class ProductAdapter(
    private val onAddToCartClick: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, ProductAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.tvProductName.text = product.name
            binding.tvProductDescription.text = product.description
            binding.tvProductPrice.text = "$${product.price}"

            Glide.with(binding.ivProductImage.context)
                .load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivProductImage)

            // Click pe butonul Adaugă în coș
            binding.btnAddToCart.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(old: ProductEntity, new: ProductEntity) = old.id == new.id
        override fun areContentsTheSame(old: ProductEntity, new: ProductEntity) = old == new
    }
}

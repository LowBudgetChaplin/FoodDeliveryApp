package com.example.fooddeliveryapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.model.CartItemEntity

class CartAdapter(
    private val onDeleteClick: (CartItemEntity) -> Unit,
    private val onQuantityChange: (CartItemEntity, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: MutableList<CartItemEntity> = mutableListOf()

    fun submitList(items: List<CartItemEntity>) {
        cartItems = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductId: TextView = itemView.findViewById(R.id.tv_product_id)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        private val btnIncrease: Button = itemView.findViewById(R.id.btn_increase)
        private val btnDecrease: Button = itemView.findViewById(R.id.btn_decrease)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_item)

        fun bind(item: CartItemEntity) {
            tvProductId.text = "Produs: ${item.productId}"
            tvQuantity.text = item.quantity.toString()

            btnIncrease.setOnClickListener {
                item.quantity++
                tvQuantity.text = item.quantity.toString()
                onQuantityChange(item, item.quantity)
            }

            btnDecrease.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    tvQuantity.text = item.quantity.toString()
                    onQuantityChange(item, item.quantity)
                }
            }

            btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(item)
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}

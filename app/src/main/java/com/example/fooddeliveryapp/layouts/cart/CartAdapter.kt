package com.example.fooddeliveryapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.model.CartItemEntity
import com.example.fooddeliveryapp.entities.model.CartItemWithProduct


class CartAdapter(
    private val onDeleteClick: (CartItemEntity) -> Unit,
    private val onQuantityChange: (CartItemEntity, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: MutableList<CartItemWithProduct> = mutableListOf()

    fun submitList(items: List<CartItemWithProduct>) {
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
        val itemWithProduct = cartItems[position]
        holder.bind(itemWithProduct)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_id)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        private val btnIncrease: Button = itemView.findViewById(R.id.btn_increase)
        private val btnDecrease: Button = itemView.findViewById(R.id.btn_decrease)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_item)

        fun bind(itemWithProduct: CartItemWithProduct) {
            tvProductName.text = "${itemWithProduct.productName}"
            tvQuantity.text = itemWithProduct.cartItem.quantity.toString()

            btnIncrease.setOnClickListener {
                val cartItem = itemWithProduct.cartItem
                cartItem.quantity++
                tvQuantity.text = cartItem.quantity.toString()
                onQuantityChange(cartItem, cartItem.quantity)
            }

            btnDecrease.setOnClickListener {
                val cartItem = itemWithProduct.cartItem
                if (cartItem.quantity > 1) {
                    cartItem.quantity--
                    tvQuantity.text = cartItem.quantity.toString()
                    onQuantityChange(cartItem, cartItem.quantity)
                }
            }

            btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(itemWithProduct.cartItem)
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}
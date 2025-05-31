package com.example.fooddeliveryapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.databinding.ItemOrderBinding
import com.example.fooddeliveryapp.entities.model.OrderEntity
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(private val orders: List<OrderEntity>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderEntity) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            binding.tvOrderId.text = "Comanda #${order.id}"
            binding.tvOrderDate.text = "Data: ${dateFormat.format(order.date)}"
//            binding.tvOrderStatus.text = "Status: ${order.status}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size
}

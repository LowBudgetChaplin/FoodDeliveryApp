package com.example.fooddeliveryapp.layouts

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.databinding.ActivityOrderHistoryBinding
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.utils.OrderAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import android.view.View

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = "Istoric comenzi"

        val db = AppDatabase.getInstance(this)
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = prefs.getLong("userId", -1)
        Log.d("OrderHistory", "Using userId = $userId")

        lifecycleScope.launch {
            val orders = withContext(Dispatchers.IO) {
                db.orderDao().getUserOrdersDesc(userId)
            }

            Log.d("OrderHistory", "Found ${orders.size} orders")
            orders.forEach {
                Log.d("OrderHistory", "Order ID: ${it.id}, Date: ${it.date}, Status: ${it.status}")
            }

            Log.d("OrderHistory", "Found ${orders.size} orders")

            if (orders.isEmpty()) {
                binding.tvNoOrders.visibility = View.VISIBLE
                binding.rvOrders.visibility = View.GONE
            } else {
                binding.tvNoOrders.visibility = View.GONE
                binding.rvOrders.visibility = View.VISIBLE
                binding.rvOrders.layoutManager = LinearLayoutManager(this@OrderHistoryActivity)
                binding.rvOrders.adapter = OrderAdapter(orders)
            }

//            binding.rvOrders.layoutManager = LinearLayoutManager(this@OrderHistoryActivity)
//            binding.rvOrders.adapter = OrderAdapter(orders)
        }
    }
}
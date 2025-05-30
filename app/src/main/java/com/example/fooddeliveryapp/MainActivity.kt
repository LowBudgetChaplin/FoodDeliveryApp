package com.example.fooddeliveryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.databinding.ActivityMainBinding
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.layouts.CurrentOrderActivity
import com.example.fooddeliveryapp.layouts.OrderHistoryActivity
import com.example.fooddeliveryapp.utils.RestaurantAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RestaurantAdapter { product ->
            Log.d("MainActivity", "Clicked restaurant: ${product.name}")
        }
        binding.rvRestaurants.layoutManager = LinearLayoutManager(this)
        binding.rvRestaurants.adapter = adapter

        val db = AppDatabase.getInstance(this)
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                db.productDao().getAll()
            }
            adapter.submitList(list)
            Log.d("MainActivity", "Loaded ${list.size} restaurants")
        }

        binding.searchRestaurants.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        binding.btnTrackDelivery.setOnClickListener {
            startActivity(Intent(this, CurrentOrderActivity::class.java))
        }
        binding.btnOrderHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
        binding.btnCurrentCart.setOnClickListener {}
    }
}
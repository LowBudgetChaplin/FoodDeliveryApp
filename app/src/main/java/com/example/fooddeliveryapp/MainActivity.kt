package com.example.fooddeliveryapp

import android.content.Context
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
import com.example.fooddeliveryapp.layouts.ProductListActivity
import com.example.fooddeliveryapp.layouts.cart.CartActivity
import com.example.fooddeliveryapp.layouts.base.LoginActivity
import com.example.fooddeliveryapp.utils.RestaurantAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.searchRestaurants.setIconifiedByDefault(false)
        binding.searchRestaurants.isIconified = false
        binding.searchRestaurants.clearFocus()
        setContentView(binding.root)

        adapter = RestaurantAdapter { restaurant ->
            try {
                Log.d("MainActivity", "Clicked restaurant: ${restaurant.name}")

                val intent = Intent(this, ProductListActivity::class.java)
                intent.putExtra("restaurant_id", restaurant.id)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting ProductListActivity", e)
            }
        }

        binding.btnLogout.setOnClickListener {
            getSharedPreferences("user_session", Context.MODE_PRIVATE)
                .edit {
                    clear()
                }

            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }


        binding.rvRestaurants.layoutManager = LinearLayoutManager(this)
        binding.rvRestaurants.adapter = adapter


        val db = AppDatabase.getInstance(this)
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                db.restaurantDao().getAllRestaurants()
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
        binding.btnCurrentCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

    }

}
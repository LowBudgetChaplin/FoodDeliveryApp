package com.example.fooddeliveryapp.layouts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.ProductDao
import com.example.fooddeliveryapp.utils.ProductAdapter
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    private lateinit var productDao: ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) {
            Log.d("da","da");
            finish()
            return
        }


        recyclerView = findViewById(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter()
        recyclerView.adapter = adapter

        productDao = AppDatabase.getInstance(applicationContext).productDao()

        loadProducts(restaurantId)
    }

    private fun loadProducts(restaurantId: Int) {
        lifecycleScope.launch {
            try {
                val products = productDao.getProductsByRestaurant(restaurantId)
                adapter.submitList(products)
            } catch (e: Exception) {
                Toast.makeText(this@ProductListActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


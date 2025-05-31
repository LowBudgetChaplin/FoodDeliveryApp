package com.example.fooddeliveryapp.layouts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.ProductDao
import com.example.fooddeliveryapp.layouts.cart.CartRepository
import com.example.fooddeliveryapp.layouts.cart.CartViewModel
import com.example.fooddeliveryapp.layouts.cart.CartViewModelFactory
import com.example.fooddeliveryapp.utils.ProductAdapter
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productDao: ProductDao

    private val cartViewModel: CartViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        val repository = CartRepository(
            db.cartDao(),
            db.orderDao(),
            db.orderItemDao(),
            db.productDao()
        )
        CartViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val restaurantId = intent.getIntExtra("restaurant_id", -1)
        if (restaurantId == -1) {
            finish()
            return
        }

        recyclerView = findViewById(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter { product ->
            cartViewModel.addToCart(product.id)
            Toast.makeText(this, "Adaugat in cos: ${product.name}", Toast.LENGTH_SHORT).show()
        }

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
                Toast.makeText(
                    this@ProductListActivity,
                    "Failed to load products",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

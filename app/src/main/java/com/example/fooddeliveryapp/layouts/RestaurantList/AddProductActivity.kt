package com.example.fooddeliveryapp.layouts.RestaurantList

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.model.ProductEntity
import com.example.fooddeliveryapp.entities.model.RestaurantEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {

    private val products = mutableListOf<ProductEntity>()
    private lateinit var productNameEditText: EditText
    private lateinit var productDescEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var productCategoryEditText: EditText
    private lateinit var productImageUrlEditText: EditText
    private lateinit var btnAddProduct: Button
    private lateinit var btnFinish: Button

    private lateinit var db: AppDatabase

    private lateinit var restaurantName: String
    private lateinit var restaurantCategory: String
    private lateinit var restaurantImageUrl: String
    private lateinit var restaurantAddress: String
    private lateinit var restaurantDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        restaurantName = intent.getStringExtra("restaurantName") ?: ""
        restaurantCategory = intent.getStringExtra("restaurantCategory") ?: ""
        restaurantImageUrl = intent.getStringExtra("restaurantImageUrl") ?: ""
        restaurantAddress = intent.getStringExtra("restaurantAddress") ?: ""
        restaurantDescription = intent.getStringExtra("restaurantDescription") ?: ""

        db = AppDatabase.getInstance(this)

        productNameEditText = findViewById(R.id.etProductName)
        productDescEditText = findViewById(R.id.etProductDescription)
        productPriceEditText = findViewById(R.id.etProductPrice)
        productCategoryEditText = findViewById(R.id.etProductCategory)
        productImageUrlEditText = findViewById(R.id.etProductImageUrl)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnFinish = findViewById(R.id.btnFinishRestaurant)

        btnAddProduct.setOnClickListener {
            val name = productNameEditText.text.toString()
            val desc = productDescEditText.text.toString()
            val price = productPriceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val category = productCategoryEditText.text.toString()
            val productImageUrl = productImageUrlEditText.text.toString()

            if (name.isNotEmpty() && price > 0) {
                val product = ProductEntity(
                    restaurantId = 0,
                    name = name,
                    description = desc,
                    price = price,
                    imageUrl = productImageUrl,
                    category = category
                )
                products.add(product)
                Toast.makeText(this, "Produs adăugat", Toast.LENGTH_SHORT).show()

                productNameEditText.text.clear()
                productDescEditText.text.clear()
                productPriceEditText.text.clear()
                productCategoryEditText.text.clear()
                productImageUrlEditText.text.clear()
            } else {
                Toast.makeText(this, "Completeaza corect campurile produsului", Toast.LENGTH_SHORT).show()
            }
        }

        btnFinish.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val restaurant = RestaurantEntity(
                    name = restaurantName,
                    category = restaurantCategory,
                    imageUrl = restaurantImageUrl,
                    address = restaurantAddress,
                    description = restaurantDescription
                )

                val restaurantId = db.restaurantDao().insertRestaurant(restaurant)

                products.forEach { product ->
                    db.productDao().insertProduct(
                        product.copy(restaurantId = restaurantId)
                    )
                }

                runOnUiThread {
                    Toast.makeText(this@AddProductActivity, "Restaurant creat cu ${products.size} produse", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddProductActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

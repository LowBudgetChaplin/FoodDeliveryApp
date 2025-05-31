package com.example.fooddeliveryapp.layouts.RestaurantList

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.RestaurantDao
import com.example.fooddeliveryapp.entities.model.RestaurantEntity
import com.example.fooddeliveryapp.layouts.RestaurantList.RestaurantsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRestaurantActivity : AppCompatActivity() {

    private lateinit var restaurantDao: RestaurantDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        restaurantDao = AppDatabase.getInstance(this).restaurantDao()

        val nameEditText = findViewById<EditText>(R.id.etRestaurantName)
        val categoryEditText = findViewById<EditText>(R.id.etCategory)
        val imageUrlEditText = findViewById<EditText>(R.id.etImageUrl)
        val btnAddRestaurant = findViewById<Button>(R.id.btnAddRestaurant)
        val btnGoToMain = findViewById<Button>(R.id.btnGoToMain)

        btnAddRestaurant.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val category = categoryEditText.text.toString().trim()
            val imageUrl = imageUrlEditText.text.toString().trim()

            if (name.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Completeaza toate campurile obligatorii", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val restaurant = RestaurantEntity(name = name, category = category, imageUrl = imageUrl)
                restaurantDao.insertRestaurant(restaurant)
                runOnUiThread {
                    Toast.makeText(this@AddRestaurantActivity, "Restaurant adaugat cu succes", Toast.LENGTH_SHORT).show()
                    nameEditText.text.clear()
                    categoryEditText.text.clear()
                    imageUrlEditText.text.clear()
                }
            }
        }

        btnGoToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
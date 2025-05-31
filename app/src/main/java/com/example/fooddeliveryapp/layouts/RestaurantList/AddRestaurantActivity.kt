package com.example.fooddeliveryapp.layouts.RestaurantList

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.RestaurantDao

class AddRestaurantActivity : AppCompatActivity() {

    private lateinit var restaurantDao: RestaurantDao
    private lateinit var nameEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var btnAddRestaurant: Button
    private lateinit var btnGoToMain: Button
    private lateinit var addressEditText: EditText
    private lateinit var descriptionEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        restaurantDao = AppDatabase.getInstance(this).restaurantDao()
        nameEditText = findViewById(R.id.etRestaurantName)
        categoryEditText = findViewById(R.id.etCategory)
        imageUrlEditText = findViewById(R.id.etImageUrl)
        btnAddRestaurant = findViewById(R.id.btnAddRestaurant)
        btnGoToMain = findViewById(R.id.btnGoToMain)
        addressEditText = findViewById(R.id.etRestaurantAddress)
        descriptionEditText = findViewById(R.id.etRestaurantDescription)

        btnAddRestaurant.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val category = categoryEditText.text.toString().trim()
            val imageUrl = imageUrlEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (name.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Completeaza toate campurile obligatorii", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("restaurantName", name)
            intent.putExtra("restaurantCategory", category)
            intent.putExtra("restaurantImageUrl", imageUrl)
            intent.putExtra("restaurantAddress", address)
            intent.putExtra("restaurantDescription", description)
            startActivity(intent)
            finish()
        }

        btnGoToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            startActivity(intent)
            finish()
        }
    }
}
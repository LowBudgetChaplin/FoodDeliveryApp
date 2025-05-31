package com.example.fooddeliveryapp.layouts.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.layouts.RestaurantList.AddRestaurantActivity

class SplitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val role = prefs.getString("role", null)

        if (role == "client") {
            startActivity(Intent(this, MainActivity::class.java))
        } else if (role == "manager") {
            startActivity(Intent(this, AddRestaurantActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}
package com.example.fooddeliveryapp.layouts.cart

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.fragments.CartFragment

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportFragmentManager.beginTransaction()
            .replace(R.id.cart_fragment_root, CartFragment())
            .commit()


    }


}

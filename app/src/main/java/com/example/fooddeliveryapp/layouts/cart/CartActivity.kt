package com.example.fooddeliveryapp.layouts.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.R

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportFragmentManager.beginTransaction()
            .replace(R.id.cart_fragment_root, CartFragment())
            .commit()


    }


}

package com.example.fooddeliveryapp.layouts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.databinding.ActivityCurrentOrderBinding

class CurrentOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = "Comanda in curs"
    }
}

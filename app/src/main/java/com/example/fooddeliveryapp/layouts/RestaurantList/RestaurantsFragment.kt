package com.example.fooddeliveryapp.layouts.RestaurantList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.RestaurantDao
import com.example.fooddeliveryapp.utils.RestaurantAdapter
import kotlinx.coroutines.launch

class RestaurantsFragment : Fragment(R.layout.fragment_restaurant)

 {

    private lateinit var restaurantDao: RestaurantDao
    private lateinit var adapter: RestaurantAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RestaurantAdapter { restaurant ->
            Toast.makeText(requireContext(), "Clicked: ${restaurant.name}", Toast.LENGTH_SHORT).show()
            // aici poți naviga la pagina cu produsele restaurantului
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRestaurants)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Inițializezi restaurantDao
        restaurantDao = AppDatabase.getInstance(requireContext()).restaurantDao()

        lifecycleScope.launch {
            val restaurants = restaurantDao.getAllRestaurants()
            adapter.submitList(restaurants)
        }
    }
}

package com.example.fooddeliveryapp.layouts.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.utils.CartAdapter
import kotlinx.coroutines.flow.collectLatest

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    private val cartViewModel: CartViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        val repository = CartRepository(
            db.cartDao(),
            db.orderDao(),
            db.orderItemDao(),
            db.productDao()
        )
        CartViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_cart_items)
        adapter = CartAdapter(
            onDeleteClick = { item -> cartViewModel.deleteItemFromCart(item) },
            onQuantityChange = { item, newQuantity ->
                cartViewModel.updateItemQuantity(item, newQuantity)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItemsWithProductNames.collectLatest { itemsWithNames ->
                adapter.submitList(itemsWithNames)
            }
        }

        val placeOrderButton = view.findViewById<Button>(R.id.plasezacomandaBtn)
        placeOrderButton.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val userId = prefs.getLong("userId", -1)
            cartViewModel.placeOrder(
                userId,
                onSuccess = {
                    Toast.makeText(requireContext(), "Comanda a fost plasată!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(requireContext(), "Eroare: ${error.message}", Toast.LENGTH_LONG)
                        .show()
                }
            )
        }
    }
}
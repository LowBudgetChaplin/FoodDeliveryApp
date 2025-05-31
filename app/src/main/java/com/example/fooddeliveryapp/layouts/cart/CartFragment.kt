package com.example.fooddeliveryapp.fragments

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
import com.example.fooddeliveryapp.layouts.cart.CartRepository
import com.example.fooddeliveryapp.layouts.cart.CartViewModel
import com.example.fooddeliveryapp.layouts.cart.CartViewModelFactory
import com.example.fooddeliveryapp.utils.CartAdapter
import kotlinx.coroutines.flow.collectLatest

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    private val cartViewModel: CartViewModel by viewModels {
        // Inițializăm CartRepository cu toate DAO-urile necesare
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
            onDeleteClick = { item ->
                cartViewModel.deleteItemFromCart(item)
            },
            onQuantityChange = { item, newQuantity ->
                cartViewModel.updateItemQuantity(item, newQuantity)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observăm Flow-ul de elemente din coș
        lifecycleScope.launchWhenStarted {
            cartViewModel.cartItems.collectLatest { items ->
                adapter.submitList(items)
            }
        }

        // Butonul „Plasează Comanda”
        val placeOrderButton = view.findViewById<Button>(R.id.plasezacomandaBtn)
        placeOrderButton.setOnClickListener {
            // Să zicem că avem userId fix (de exemplu 1L).
            val userId = 1L
            cartViewModel.placeOrder(
                userId,
                onSuccess = {
                    Toast.makeText(requireContext(), "Comanda a fost plasată!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(requireContext(), "Eroare la plasarea comenzii: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

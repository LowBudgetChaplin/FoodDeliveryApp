package com.example.fooddeliveryapp.layouts.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.entities.model.CartItemEntity
import com.example.fooddeliveryapp.entities.model.OrderEntity
import com.example.fooddeliveryapp.entities.model.OrderItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val cartItems = cartRepository.cartItems
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addToCart(productId: Long) {
        viewModelScope.launch {
            cartRepository.addOrUpdateCartItem(productId)
        }
    }

    fun deleteItemFromCart(cartItem: CartItemEntity) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(cartItem)
        }
    }

    fun updateItemQuantity(cartItem: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                cartRepository.deleteCartItem(cartItem)
            } else {
                cartRepository.updateCartItemQuantity(cartItem, newQuantity)
            }
        }
    }

    /**
     * Plasează comanda: citește tot ce e în coș, creează Order + OrderItems,
     * golește coșul la final. Afișează un Toast de confirmare (prin callback).
     */
    fun placeOrder(
        userId: Long,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Preia toate elementele din coș
                val itemsInCart = cartRepository.getAllCartItemsOnce()

                if (itemsInCart.isEmpty()) {
                    // Dacă coșul e gol, aruncăm o excepție custom
                    throw IllegalStateException("Coșul este gol")
                }

                // 2. Creează un nou OrderEntity
                val newOrder = OrderEntity(
                    userId = userId,
                    date = Date(),
                    status = "NEW"
                )
                val orderId = cartRepository.insertOrder(newOrder)

                // 3. Pentru fiecare CartItemEntity, ia produsul ca să afli prețul
                for (cartItem in itemsInCart) {
                    val product = cartRepository.getProductById(cartItem.productId)
                    if (product != null) {
                        val orderItem = OrderItemEntity(
                            orderId = orderId,
                            productId = product.id,
                            quantity = cartItem.quantity,
                            priceAtPurchase = product.price
                        )
                        cartRepository.insertOrderItem(orderItem)
                    }
                }

                // 4. Golește coșul
                cartRepository.clearCart()

                // 5. Rulează callback-ul onSuccess pe thread-ul principal
                launch(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }
}

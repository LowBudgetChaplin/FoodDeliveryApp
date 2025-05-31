package com.example.fooddeliveryapp.layouts.cart

import com.example.fooddeliveryapp.entities.dao.*
import com.example.fooddeliveryapp.entities.dao.CartDao
import com.example.fooddeliveryapp.entities.model.*
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val productDao: ProductDao
) {

    val cartItems: Flow<List<CartItemEntity>> = cartDao.getAll()

    suspend fun addOrUpdateCartItem(productId: Long) {
        val existingItem = cartDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.updateCartItem(updatedItem)
        } else {
            cartDao.insertCartItem(CartItemEntity(productId = productId, quantity = 1))
        }
    }

    suspend fun deleteCartItem(cartItem: CartItemEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun updateCartItemQuantity(cartItem: CartItemEntity, quantity: Int) {
        val updatedItem = cartItem.copy(quantity = quantity)
        cartDao.updateCartItem(updatedItem)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // ---------- Metode pentru placeOrder ----------

    /** Returnează o listă simplă (non-Flow) cu elementele din coș, odată. */
    suspend fun getAllCartItemsOnce(): List<CartItemEntity> {
        return cartDao.getAllOnce() // vezi DAO modificat mai jos
    }

    /** Inserează Order și returnează orderId-ul */
    suspend fun insertOrder(order: OrderEntity): Long {
        return orderDao.insert(order)
    }

    /** Inserează un OrderItem */
    suspend fun insertOrderItem(orderItem: OrderItemEntity): Long {
        return orderItemDao.insert(orderItem)
    }

    /** Returnează ProductEntity după ID */
    suspend fun getProductById(id: Long): ProductEntity? {
        return productDao.getById(id)
    }
}

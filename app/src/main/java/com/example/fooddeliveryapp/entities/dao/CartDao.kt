package com.example.fooddeliveryapp.entities.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    fun getAll(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items")
    suspend fun getAllOnce(): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: Long): CartItemEntity?

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

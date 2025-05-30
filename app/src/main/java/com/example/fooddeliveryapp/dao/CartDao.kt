package com.example.fooddeliveryapp.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.CartItemEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: CartItemEntity): Long

    @Update
    suspend fun update(item: CartItemEntity)

    @Delete
    suspend fun remove(item: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    suspend fun getAll(): List<CartItemEntity>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
package com.example.fooddeliveryapp.entities.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.OrderEntity
import com.example.fooddeliveryapp.entities.model.OrderItemEntity
import com.example.fooddeliveryapp.entities.model.ProductEntity
import com.example.fooddeliveryapp.entities.model.RestaurantEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Update
    suspend fun update(order: OrderEntity)

    @Delete
    suspend fun delete(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getById(id: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getByUser(userId: Long): List<OrderEntity>

    @Query("SELECT * FROM orders")
    suspend fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    suspend fun getLastOrderForUser(userId: Long): OrderEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItems(orderId: Long): List<OrderItemEntity>

    @Query("""
    SELECT SUM(quantity * priceAtPurchase) 
    FROM order_items 
    WHERE orderId = :orderId""")
    suspend fun getTotalForOrder(orderId: Long): Double?

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    suspend fun getUserOrdersDesc(userId: Long): List<OrderEntity>
}
package com.example.fooddeliveryapp.entities.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.OrderEntity

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
}
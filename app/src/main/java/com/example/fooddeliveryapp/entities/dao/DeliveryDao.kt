package com.example.fooddeliveryapp.entities.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.DeliveryLocationEntity

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logLocation(loc: DeliveryLocationEntity): Long

    @Query("""
      SELECT * FROM delivery_locations
      WHERE orderId = :orderId
      ORDER BY timestamp ASC
    """)
    suspend fun getRoute(orderId: Long): List<DeliveryLocationEntity>
}
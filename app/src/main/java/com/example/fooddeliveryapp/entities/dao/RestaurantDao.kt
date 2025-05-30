package com.example.fooddeliveryapp.entities.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddeliveryapp.entities.model.RestaurantEntity

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg items: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<RestaurantEntity>
}
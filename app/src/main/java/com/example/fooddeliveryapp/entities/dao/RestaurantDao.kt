package com.example.fooddeliveryapp.entities.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddeliveryapp.entities.model.RestaurantEntity

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    suspend fun getAllRestaurants(): List<RestaurantEntity>
}
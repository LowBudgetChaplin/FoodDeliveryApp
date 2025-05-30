package com.example.fooddeliveryapp.entities.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val imageUrl: String
)
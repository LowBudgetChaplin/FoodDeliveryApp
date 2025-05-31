package com.example.fooddeliveryapp.entities.dao

import androidx.room.*
import com.example.fooddeliveryapp.entities.model.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Long): ProductEntity?

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE restaurantId = :restaurantId")
    suspend fun getProductsByRestaurant(restaurantId: Int): List<ProductEntity>

    @Query("SELECT * FROM products WHERE category = :category")
    suspend fun getByCategory(category: String): List<ProductEntity>
}
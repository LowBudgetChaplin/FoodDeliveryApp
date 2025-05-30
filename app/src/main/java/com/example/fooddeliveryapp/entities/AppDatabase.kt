package com.example.fooddeliveryapp.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fooddeliveryapp.dao.CartDao
import com.example.fooddeliveryapp.dao.DeliveryDao
import com.example.fooddeliveryapp.dao.OrderDao
import com.example.fooddeliveryapp.dao.ProductDao
import com.example.fooddeliveryapp.dao.UserDao
import com.example.fooddeliveryapp.utils.Converters
import com.example.fooddeliveryapp.entities.model.*

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        DeliveryLocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun deliveryDao(): DeliveryDao
}
package com.example.fooddeliveryapp.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fooddeliveryapp.entities.dao.*
import com.example.fooddeliveryapp.entities.model.CartItemEntity
import com.example.fooddeliveryapp.entities.model.DeliveryLocationEntity
import com.example.fooddeliveryapp.entities.model.OrderEntity
import com.example.fooddeliveryapp.entities.model.OrderItemEntity
import com.example.fooddeliveryapp.entities.model.ProductEntity
import com.example.fooddeliveryapp.utils.Converters
import com.example.fooddeliveryapp.entities.model.RestaurantEntity
import com.example.fooddeliveryapp.entities.model.UserEntity




@Database(
    entities = [
        UserEntity::class,
        RestaurantEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        DeliveryLocationEntity::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun deliveryDao(): DeliveryDao
    abstract fun orderItemDao(): OrderItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_delivery.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
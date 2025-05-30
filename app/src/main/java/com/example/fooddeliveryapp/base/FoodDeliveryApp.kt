package com.example.fooddeliveryapp.base

import android.app.Application
import androidx.room.Room
import com.example.fooddeliveryapp.entities.AppDatabase


class FoodDeliveryApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "food_delivery.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

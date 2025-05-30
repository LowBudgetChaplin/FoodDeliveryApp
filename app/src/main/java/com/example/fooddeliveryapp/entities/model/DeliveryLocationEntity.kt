package com.example.fooddeliveryapp.entities.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "delivery_locations",
    primaryKeys = ["orderId", "timestamp"],
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class DeliveryLocationEntity(
    val orderId: Long,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Date
)
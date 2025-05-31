package com.example.fooddeliveryapp.utils

import android.content.Context
import com.example.fooddeliveryapp.entities.AppDatabase

object OrderSummary {

    suspend fun generateOrderSummary(context: Context, userId: Long): String {
        val db = AppDatabase.getInstance(context)
        val orderDao = db.orderDao()
        val productDao = db.productDao()

        val lastOrder = orderDao.getLastOrderForUser(userId) ?: return "Nicio comanda recenta"
        val items = orderDao.getOrderItems(lastOrder.id)

        val itemSummaries = items.mapNotNull { item ->
            val productName = productDao.getProductNameById(item.productId)
            productName?.let { "$it x${item.quantity}" }
        }

        val total = items.sumOf { it.quantity * it.priceAtPurchase }
        return "${itemSummaries.joinToString(", ")} – Total: %.2f RON".format(total)
    }
}
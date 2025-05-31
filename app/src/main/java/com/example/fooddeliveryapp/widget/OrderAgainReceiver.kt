package com.example.fooddeliveryapp.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.model.OrderEntity
import com.example.fooddeliveryapp.utils.OrderSummary
import kotlinx.coroutines.*
import java.util.*
import android.app.PendingIntent

class OrderAgainReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "com.example.fooddeliveryapp.ACTION_ORDER_AGAIN") return

        val db = AppDatabase.getInstance(context)
        val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = prefs.getLong("userId", -1)

        if (userId == -1L) {
            Toast.makeText(context, "Utilizator neautentificat", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val lastOrder = db.orderDao().getUserOrdersDesc(userId).firstOrNull()

            if (lastOrder != null) {
                val items = db.orderItemDao().getByOrder(lastOrder.id)
                val newOrder = OrderEntity(
                    userId = userId,
                    date = Date(),
                    status = "Comanda plasata din nou"
                )
                val newOrderId = db.orderDao().insert(newOrder)

                for (item in items) {
                    db.orderItemDao().insert(item.copy(id = 0, orderId = newOrderId))
                }

                val summary = OrderSummary.generateOrderSummary(context, userId)

                withContext(Dispatchers.Main) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(context, OrderAgainWidget::class.java)
                    )
                    for (appWidgetId in appWidgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_order_again)
                        views.setTextViewText(R.id.tvOrderSummary, summary)

                        val intent = Intent(context, OrderAgainReceiver::class.java).apply {
                            action = "com.example.fooddeliveryapp.ACTION_ORDER_AGAIN"
                        }

                        val pendingIntent = PendingIntent.getBroadcast(
                            context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )

                        views.setOnClickPendingIntent(R.id.btnOrderAgain, pendingIntent)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }

                    Toast.makeText(context, "Comanda a fost plasată din nou!", Toast.LENGTH_SHORT).show()

                    val launchIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra("orderRedone", true)
                    }
                    context.startActivity(launchIntent)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Nicio comandă anterioară!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
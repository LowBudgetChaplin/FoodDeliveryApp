package com.example.fooddeliveryapp.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.fooddeliveryapp.R
import kotlinx.coroutines.*
import com.example.fooddeliveryapp.utils.OrderSummary

class OrderAgainWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            CoroutineScope(Dispatchers.IO).launch {
                val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val userId = prefs.getLong("userId", -1)

                val summary = if (userId != -1L) {
                    OrderSummary.generateOrderSummary(context, userId)
                } else {
                    "Nicio comanda disponibila"
                }

                withContext(Dispatchers.Main) {
                    val views = RemoteViews(context.packageName, R.layout.widget_order_again)
                    views.setTextViewText(R.id.tvOrderSummary, summary)

                    val intent = Intent(context, OrderAgainReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.btnOrderAgain, pendingIntent)

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}
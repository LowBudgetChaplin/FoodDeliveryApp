package com.example.fooddeliveryapp.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.fooddeliveryapp.MainActivity

class OrderAgainReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val launchIntent = Intent(context, MainActivity::class.java)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(launchIntent)

        Toast.makeText(context, "Refacem comanda...", Toast.LENGTH_SHORT).show()
    }
}
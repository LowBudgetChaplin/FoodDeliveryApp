package com.example.fooddeliveryapp.layouts.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddeliveryapp.MainActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.entities.AppDatabase
import com.example.fooddeliveryapp.entities.dao.UserDao
import com.example.fooddeliveryapp.layouts.RestaurantList.AddRestaurantActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDao = AppDatabase.getInstance(this).userDao()

        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerText = findViewById<TextView>(R.id.tvGoToRegister)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completeaza toate campurile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.getUserByCredentials(email, password)
                runOnUiThread {
                    if (user != null) {
                        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putLong("userId", user.id)
                            .putString("role", user.role)
                            .apply()

                        val intent = if (user.role.lowercase() == "client") {
                            Intent(this@LoginActivity, MainActivity::class.java)
                        } else {
                            Intent(this@LoginActivity, AddRestaurantActivity::class.java)
                        }

                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Email sau parola gresite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
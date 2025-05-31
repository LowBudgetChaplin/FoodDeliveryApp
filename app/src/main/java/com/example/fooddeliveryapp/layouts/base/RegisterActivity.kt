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
import com.example.fooddeliveryapp.entities.model.UserEntity
import com.example.fooddeliveryapp.layouts.RestaurantList.AddRestaurantActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userDao = AppDatabase.getInstance(this).userDao()

        val nameEditText = findViewById<EditText>(R.id.etName)
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val phoneEditText = findViewById<EditText>(R.id.etPhone)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val roleSpinner = findViewById<Spinner>(R.id.spinnerRole)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        val roles = listOf("Client", "Manager de restaurant")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completeaza toate campurile", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val existingUser = userDao.getUserByCredentials(email, password)
                if (existingUser != null) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "User deja inregistrat", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val newUser = UserEntity(name = name, email = email, password = password, phone = phone ,role = role)
                    userDao.insertUser(newUser)

                    val insertedUser = userDao.getUserByCredentials(email, password)
                    insertedUser?.let { user ->
                        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putLong("userId", user.id)
                            .putString("role", user.role.lowercase())
                            .apply()

                        runOnUiThread {
                            if (role == "Client") {
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            } else {
                                startActivity(Intent(this@RegisterActivity, AddRestaurantActivity::class.java))
                            }
                            finish()
                        }
                    }
                }
            }
        }

        tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
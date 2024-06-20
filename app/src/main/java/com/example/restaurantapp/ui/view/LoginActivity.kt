package com.example.restaurantapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.data.repository.UserRepository
import com.example.restaurantapp.data.room.AppDatabase
import com.example.restaurantapp.ui.viewmodel.UserViewModel
import com.example.restaurantapp.ui.viewmodel.UserViewModelFactory
import com.example.restaurantapp.utils.PreferenceHelper
import com.example.restaurantapp.utils.Status

class LoginActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(AppDatabase.getDatabase(application).userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.login(username, password).observe(this, { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            PreferenceHelper.setLoggedIn(this, true)
                            PreferenceHelper.setUsername(this, username)
                            startActivity(Intent(this, RestaurantListActivity::class.java))
                            finish()
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                            // Handle loading state if needed
                        }
                    }
                })
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

package com.example.restaurantapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.data.model.User
import com.example.restaurantapp.data.room.AppDatabase
import com.example.restaurantapp.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etBio: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etBio = findViewById(R.id.etBio)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etAddress = findViewById(R.id.etAddress)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val bio = etBio.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()
            val address = etAddress.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(username, email, password, bio, phoneNumber, address)
            }
        }
    }

    private fun registerUser(
        username: String,
        email: String,
        password: String,
        bio: String,
        phoneNumber: String,
        address: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = AppDatabase.getDatabase(application).userDao()
            val existingUser = userDao.checkUserExists(username)

            if (existingUser != null) {
                runOnUiThread {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Username sudah digunakan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val user = User(
                    username = username,
                    password = password,
                    email = email,
                    bio = bio,
                    phoneNumber = phoneNumber,
                    address = address
                )
                userDao.insert(user)

                val sharedPreferences =
                    PreferenceHelper.customPreference(this@RegisterActivity, "user")
                sharedPreferences.edit().apply {
                    putString("username", username)
                    apply()
                }

                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.ui.view.LoginActivity
import com.example.restaurantapp.ui.view.RestaurantListActivity
import com.example.restaurantapp.utils.PreferenceHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferenceHelper.isLoggedIn(this)) {
            val intent = Intent(this, RestaurantListActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

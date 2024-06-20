package com.example.restaurantapp.ui.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.viewmodel.RestaurantViewModel
import com.example.restaurantapp.utils.Status

class AddReviewActivity : AppCompatActivity() {
    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val etReview: EditText = findViewById(R.id.etReview)
        val btnSubmitReview: Button = findViewById(R.id.btnSubmitReview)

        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return

        btnSubmitReview.setOnClickListener {
            val username = etUsername.text.toString()
            val reviewText = etReview.text.toString()

            restaurantViewModel.addReview(restaurantId, username, reviewText).observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        // Show loading indicator if needed
                    }
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

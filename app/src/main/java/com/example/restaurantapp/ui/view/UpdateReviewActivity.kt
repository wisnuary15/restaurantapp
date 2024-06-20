package com.example.restaurantapp.ui.view

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.viewmodel.RestaurantViewModel
import com.example.restaurantapp.utils.Status

class UpdateReviewActivity : AppCompatActivity() {
    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_review)

        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return
        val review_id = intent.getStringExtra("REVIEW_ID") ?: return
        val currentReviewText = intent.getStringExtra("CURRENT_REVIEW_TEXT") ?: ""

        val etUpdateReview: EditText = findViewById(R.id.etUpdateReview)
        val btnSubmitUpdatedReview: Button = findViewById(R.id.btnSubmitUpdatedReview)

        etUpdateReview.setText(currentReviewText)

        btnSubmitUpdatedReview.setOnClickListener {
            val updatedReviewText = etUpdateReview.text.toString()
            restaurantViewModel.updateReview(restaurantId, review_id, updatedReviewText).observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Review updated successfully", Toast.LENGTH_SHORT).show()
                        val resultIntent = intent.apply {
                            putExtra("REVIEW_ID", review_id)
                            putExtra("UPDATED_REVIEW_TEXT", updatedReviewText)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
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
}

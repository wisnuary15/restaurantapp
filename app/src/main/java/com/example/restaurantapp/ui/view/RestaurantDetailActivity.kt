package com.example.restaurantapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.viewmodel.RestaurantViewModel
import com.example.restaurantapp.utils.Status

class RestaurantDetailActivity : AppCompatActivity() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var restaurantImage: ImageView
    private lateinit var restaurantName: TextView
    private lateinit var restaurantDescription: TextView
    private lateinit var restaurantAddress: TextView
    private lateinit var restaurantCategories: TextView
    private lateinit var restaurantMenus: TextView
    private lateinit var btnAddReview: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    private val addReviewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            refreshReviews()
        }
    }

    private val updateReviewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                val updatedReviewId = data.getStringExtra("REVIEW_ID")
                val updatedReviewText = data.getStringExtra("UPDATED_REVIEW_TEXT")
                if (updatedReviewId != null && updatedReviewText != null) {
                    reviewAdapter.updateReviewText(updatedReviewId, updatedReviewText)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        restaurantImage = findViewById(R.id.restaurantImage)
        restaurantName = findViewById(R.id.restaurantName)
        restaurantDescription = findViewById(R.id.restaurantDescription)
        restaurantAddress = findViewById(R.id.restaurantAddress)
        restaurantCategories = findViewById(R.id.restaurantCategories)
        restaurantMenus = findViewById(R.id.restaurantMenus)
        btnAddReview = findViewById(R.id.btnAddReview)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return

        reviewAdapter = ReviewAdapter(emptyList(), this, restaurantId, { restId, review_id, currentReviewText ->
            val intent = Intent(this, UpdateReviewActivity::class.java).apply {
                putExtra("RESTAURANT_ID", restId)
                putExtra("REVIEW_ID", review_id)
                putExtra("CURRENT_REVIEW_TEXT", currentReviewText)
            }
            updateReviewLauncher.launch(intent)
        }, { restId, review_id ->
            restaurantViewModel.deleteReview(restId, review_id).observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Review deleted successfully", Toast.LENGTH_SHORT).show()
                        refreshReviews()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        // Show loading indicator if needed
                    }
                }
            })
        })
        recyclerView.adapter = reviewAdapter

        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        restaurantViewModel.getRestaurantDetail(restaurantId).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { restaurant ->
                        restaurantName.text = restaurant.name
                        restaurantDescription.text = restaurant.description
                        restaurantAddress.text = restaurant.address
                        restaurantCategories.text = restaurant.categories.joinToString { it.name }
                        restaurantMenus.text = "Foods: ${restaurant.menus.foods.joinToString { it.name }}\nDrinks: ${restaurant.menus.drinks.joinToString { it.name }}"
                        Glide.with(this)
                            .load(restaurant.pictureId)
                            .into(restaurantImage)
                        reviewAdapter.updateData(restaurant.customerReviews)
                    }
                }
                Status.ERROR -> {
                    // Handle error
                }
                Status.LOADING -> {
                    // Show loading indicator if needed
                }
            }
        })

        btnAddReview.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java).apply {
                putExtra("RESTAURANT_ID", restaurantId)
            }
            addReviewLauncher.launch(intent)
        }
    }

    private fun refreshReviews() {
        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return
        restaurantViewModel.getRestaurantDetail(restaurantId).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { restaurant ->
                        reviewAdapter.updateData(restaurant.customerReviews)
                    }
                }
                Status.ERROR -> {
                    // Handle error
                }
                Status.LOADING -> {
                    // Show loading indicator if needed
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

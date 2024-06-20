package com.example.restaurantapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.viewmodel.RestaurantViewModel
import com.example.restaurantapp.utils.Status

class RestaurantListActivity : AppCompatActivity() {
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantAdapter
    private lateinit var etSearchQuery: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnProfile: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        setSupportActionBar(findViewById(R.id.toolbar))

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        etSearchQuery = findViewById(R.id.etSearchQuery)
        btnSearch = findViewById(R.id.btnSearch)
        btnProfile = findViewById(R.id.btnProfile)
        progressBar = findViewById(R.id.progressBar)

        adapter = RestaurantAdapter(emptyList()) { restaurant ->
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        restaurantViewModel.getRestaurants().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = ProgressBar.GONE
                    it.data?.let { restaurants ->
                        adapter.updateData(restaurants)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
            }
        })

        btnSearch.setOnClickListener {
            val query = etSearchQuery.text.toString().trim()
            if (query.isNotEmpty()) {
                searchRestaurants(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchRestaurants(query: String) {
        restaurantViewModel.searchRestaurants(query).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = ProgressBar.GONE
                    it.data?.let { restaurants ->
                        adapter.updateData(restaurants)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
            }
        })
    }
}

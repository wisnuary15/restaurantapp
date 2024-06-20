package com.example.restaurantapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.ui.viewmodel.RestaurantViewModel
import com.example.restaurantapp.utils.Status

class SearchActivity : AppCompatActivity() {
    private val restaurantViewModel: RestaurantViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val etSearchQuery: EditText = findViewById(R.id.etSearchQuery)
        val btnSearch: Button = findViewById(R.id.btnSearch)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantAdapter(emptyList()) { restaurant ->
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        btnSearch.setOnClickListener {
            val query = etSearchQuery.text.toString()
            restaurantViewModel.searchRestaurants(query).observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { restaurants ->
                            adapter.updateData(restaurants)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        // Show loading indicator if needed
                    }
                }
            })
        }
    }
}

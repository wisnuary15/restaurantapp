package com.example.restaurantapp.data.model

data class RestaurantResponse(
    val error: Boolean,
    val message: String,
    val count: Int,
    val restaurants: List<Restaurant>
)

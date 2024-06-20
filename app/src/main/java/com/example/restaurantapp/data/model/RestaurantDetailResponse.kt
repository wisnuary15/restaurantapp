package com.example.restaurantapp.data.model

data class RestaurantDetailResponse(
    val error: Boolean,
    val message: String,
    val restaurant: RestaurantDetail
)

package com.example.restaurantapp.data.model

data class RestaurantDetail(
    val id: String,
    val name: String,
    val description: String,
    val city: String,
    val address: String,
    val pictureId: String,
    val categories: List<Category>,
    val menus: Menus,
    val rating: Double,
    val customerReviews: List<CustomerReview>
)

package com.example.restaurantapp.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val pictureId: String,
    val city: String,
    val rating: Double,
    val categories: List<Category>
)

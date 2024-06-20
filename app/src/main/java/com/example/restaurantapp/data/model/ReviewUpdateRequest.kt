package com.example.restaurantapp.data.model

data class UpdateReviewRequest(
    val id: String,
    val review_id: String,
    val review: String
)

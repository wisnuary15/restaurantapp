package com.example.restaurantapp.data.model

data class ReviewResponse(
    val error: Boolean,
    val message: String,
    val customerReviews: List<CustomerReview>
)

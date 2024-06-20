package com.example.restaurantapp.data.repository

import com.example.restaurantapp.data.model.*
import com.example.restaurantapp.data.network.ApiClient

class RestaurantRepository {
    private val apiService = ApiClient.apiService

    suspend fun getRestaurants(): List<Restaurant> {
        val response = apiService.getRestaurants()
        return response.restaurants
    }

    suspend fun getRestaurantDetail(id: String): RestaurantDetail {
        val response = apiService.getRestaurantDetail(id)
        return response.restaurant
    }

    suspend fun searchRestaurants(query: String): List<Restaurant> {
        val response = apiService.searchRestaurants(query)
        return response.restaurants
    }

    suspend fun addReview(review: ReviewRequest): List<CustomerReview> {
        val response = apiService.addReview(review)
        return response.customerReviews
    }

    suspend fun updateReview(restaurantId: String, review_id: String, updatedReviewText: String): List<CustomerReview> {
        val response = apiService.updateReview(UpdateReviewRequest(restaurantId, review_id, updatedReviewText))
        return response.customerReviews
    }

    suspend fun deleteReview(restaurantId: String, review_id: String): List<CustomerReview> {
        val response = apiService.deleteReview(DeleteReviewRequest(restaurantId, review_id))
        return response.customerReviews
    }
}

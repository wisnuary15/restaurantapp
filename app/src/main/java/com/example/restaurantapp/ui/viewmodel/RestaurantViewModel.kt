package com.example.restaurantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.restaurantapp.data.repository.RestaurantRepository
import com.example.restaurantapp.data.model.ReviewRequest
import com.example.restaurantapp.utils.Resource
import kotlinx.coroutines.Dispatchers

class RestaurantViewModel : ViewModel() {
    private val repository = RestaurantRepository()

    fun getRestaurants() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.getRestaurants()
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun getRestaurantDetail(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.getRestaurantDetail(id)
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun searchRestaurants(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.searchRestaurants(query)
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun addReview(id: String, name: String, review: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.addReview(ReviewRequest(id, name, review))
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun updateReview(restaurantId: String, review_id: String, updatedReviewText: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.updateReview(restaurantId, review_id, updatedReviewText)
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteReview(restaurantId: String, review_id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val data = repository.deleteReview(restaurantId, review_id)
            emit(Resource.success(data))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }
}

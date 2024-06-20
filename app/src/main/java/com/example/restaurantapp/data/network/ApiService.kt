package com.example.restaurantapp.data.network

import com.example.restaurantapp.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.HTTP

interface ApiService {
    @GET("list")
    suspend fun getRestaurants(): RestaurantResponse

    @GET("detail/{id}")
    suspend fun getRestaurantDetail(@Path("id") id: String): RestaurantDetailResponse

    @GET("search")
    suspend fun searchRestaurants(@Query("q") query: String): RestaurantResponse

    @POST("review")
    suspend fun addReview(@Body review: ReviewRequest): ReviewResponse

    @PUT("review/update")
    suspend fun updateReview(@Body review: UpdateReviewRequest): ReviewResponse

    @HTTP(method = "DELETE", path = "review/delete", hasBody = true)
    suspend fun deleteReview(@Body review: DeleteReviewRequest): ReviewResponse
}

package com.example.restaurantapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val bio: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val profilePhotoUrl: String = ""
)

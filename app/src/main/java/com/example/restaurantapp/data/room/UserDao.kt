package com.example.restaurantapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.restaurantapp.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun checkUserExists(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): Flow<User>

    @Query("UPDATE users SET profilePhotoUrl = :photoUrl WHERE username = :username")
    suspend fun updateProfilePhoto(username: String, photoUrl: String)
}

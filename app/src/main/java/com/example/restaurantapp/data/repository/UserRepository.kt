package com.example.restaurantapp.data.repository

import com.example.restaurantapp.data.model.User
import com.example.restaurantapp.data.room.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun getUser(username: String, password: String): User? {
        return userDao.getUser(username, password)
    }

    fun getUserByUsername(username: String): Flow<User?> {
        return userDao.getUserByUsername(username)
    }

    suspend fun checkUserExists(username: String): User? {
        return userDao.checkUserExists(username)
    }

    suspend fun getUserDetails(username: String): User? {
        return userDao.checkUserExists(username)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    suspend fun updateProfilePhoto(username: String, photoUrl: String) {
        userDao.updateProfilePhoto(username, photoUrl)
    }
}

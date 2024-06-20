package com.example.restaurantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.restaurantapp.data.model.User
import com.example.restaurantapp.data.repository.UserRepository
import com.example.restaurantapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val user = repository.getUser(username, password)
            if (user != null) {
                emit(Resource.success(user))
            } else {
                emit(Resource.error(null, "Invalid username or password"))
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun getUserByUsername(username: String) = repository.getUserByUsername(username).asLiveData()

    fun getUserDetails(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val user = repository.getUserDetails(username)
            if (user != null) {
                emit(Resource.success(user))
            } else {
                emit(Resource.error(null, "User not found"))
            }
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun updateUserDetails(username: String, email: String, bio: String, phoneNumber: String, address: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            withContext(Dispatchers.IO) {
                val user = repository.getUserDetails(username)
                if (user != null) {
                    val updatedUser = user.copy(email = email, bio = bio, phoneNumber = phoneNumber, address = address)
                    repository.updateUser(updatedUser)
                }
            }
            emit(Resource.success(null))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteUser(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            withContext(Dispatchers.IO) {
                repository.deleteUser(user)
            }
            emit(Resource.success(null))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }

    fun updateProfilePhoto(username: String, photoUrl: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            repository.updateProfilePhoto(username, photoUrl)
            emit(Resource.success(null))
        } catch (exception: Exception) {
            emit(Resource.error(null, exception.message ?: "Error Occurred!"))
        }
    }
}

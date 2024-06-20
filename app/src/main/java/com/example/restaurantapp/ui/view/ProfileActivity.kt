package com.example.restaurantapp.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.restaurantapp.MainActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.data.repository.UserRepository
import com.example.restaurantapp.data.room.AppDatabase
import com.example.restaurantapp.ui.viewmodel.UserViewModel
import com.example.restaurantapp.ui.viewmodel.UserViewModelFactory
import com.example.restaurantapp.utils.PreferenceHelper
import com.example.restaurantapp.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvAddress: TextView
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var toolbar: Toolbar

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(AppDatabase.getDatabase(application).userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        tvBio = findViewById(R.id.tvBio)
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber)
        tvAddress = findViewById(R.id.tvAddress)
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        val btnEditProfile: Button = findViewById(R.id.btnEditProfile)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        val btnDeleteAccount: Button = findViewById(R.id.btnDeleteAccount) // Add this button in your layout file

        loadProfileData()

        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_EDIT_PROFILE)
        }

        btnLogout.setOnClickListener {
            PreferenceHelper.clearUserDetails(this)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnDeleteAccount.setOnClickListener {
            val username = PreferenceHelper.getUsername(this)
            if (username != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userViewModel.getUserDetails(username).value?.data
                    if (user != null) {
                        userViewModel.deleteUser(user).observe(this@ProfileActivity, Observer { resource ->
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    runOnUiThread {
                                        Toast.makeText(this@ProfileActivity, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                        PreferenceHelper.clearUserDetails(this@ProfileActivity)
                                        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                Status.ERROR -> {
                                    runOnUiThread {
                                        Toast.makeText(this@ProfileActivity, "Failed to delete account", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Status.LOADING -> {
                                    // Show loading indicator if needed
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            loadProfileData()
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileData() {
        val username = PreferenceHelper.getUsername(this)
        username?.let {
            userViewModel.getUserByUsername(it).observe(this, Observer { user ->
                user?.let {
                    tvUsername.text = "Username: ${user.username}"
                    tvEmail.text = "Email: ${user.email}"
                    tvBio.text = "Bio: ${user.bio}"
                    tvPhoneNumber.text = "Phone Number: ${user.phoneNumber}"
                    tvAddress.text = "Address: ${user.address}"
                    user.profilePhotoUrl?.let { photoUrl ->
                        Glide.with(this)
                            .load(photoUrl)
                            .placeholder(R.drawable.mercedes)
                            .circleCrop()
                            .into(ivProfilePhoto)
                    }
                }
            })
        }
    }

    companion object {
        const val REQUEST_EDIT_PROFILE = 1
    }
}

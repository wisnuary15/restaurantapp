package com.example.restaurantapp.ui.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
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
import java.io.File
import java.io.IOException

class


EditProfileActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(AppDatabase.getDatabase(application).userDao()))
    }

    private lateinit var ivProfilePhoto: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBio: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSaveProfile: Button

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto)
        progressBar = findViewById(R.id.progressBar)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etBio = findViewById(R.id.etBio)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etAddress = findViewById(R.id.etAddress)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        val username = PreferenceHelper.getUsername(this) ?: ""

        // Muat detail pengguna
        userViewModel.getUserDetails(username).observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val user = resource.data
                    etUsername.setText(user?.username)
                    etUsername.isEnabled = false
                    etEmail.setText(user?.email)
                    etBio.setText(user?.bio)
                    etPhoneNumber.setText(user?.phoneNumber)
                    etAddress.setText(user?.address)
                    user?.profilePhotoUrl?.let {
                        Glide.with(this)
                            .load(it)
                            .placeholder(R.drawable.mercedes)
                            .into(ivProfilePhoto)
                    }
                    progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })

        btnUploadPhoto.setOnClickListener {
            // Buka galeri untuk memilih gambar
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(intent)
        }

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data!!.data
                selectedImageUri?.let {
                    cropImage(it)
                }
            }
        }

        cropActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val extras = result.data!!.extras
                val bitmap = extras?.getParcelable<Bitmap>("data")
                bitmap?.let {
                    ivProfilePhoto.setImageBitmap(it)
                    saveImage(it)
                }
            }
        }

        btnSaveProfile.setOnClickListener {
            val newBio = etBio.text.toString()
            val newPhoneNumber = etPhoneNumber.text.toString()
            val newAddress = etAddress.text.toString()
            val newEmail = etEmail.text.toString()

            progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                val result = userViewModel.updateUserDetails(username, newEmail, newBio, newPhoneNumber, newAddress)
                runOnUiThread {
                    result.observe(this@EditProfileActivity, Observer { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                                finish()
                            }
                            Status.ERROR -> {
                                Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                            }
                            Status.LOADING -> {
                                progressBar.visibility = View.VISIBLE
                            }
                        }
                    })
                }
            }
        }
    }

    private fun cropImage(imageUri: Uri) {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(imageUri, "image/*")
        cropIntent.putExtra("crop", "true")
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        cropIntent.putExtra("outputX", 256)
        cropIntent.putExtra("outputY", 256)
        cropIntent.putExtra("return-data", true)
        cropActivityResultLauncher.launch(cropIntent)
    }

    private fun saveImage(bitmap: Bitmap) {
        val file = File.createTempFile(
            "profile_photo", ".jpg", filesDir
        )
        try {
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
            }
            updateProfilePhoto(Uri.fromFile(file).toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun updateProfilePhoto(photoUrl: String) {
        val username = PreferenceHelper.getUsername(this) ?: ""
        userViewModel.updateProfilePhoto(username, photoUrl).observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.mercedes)
                        .into(ivProfilePhoto)
                    progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}

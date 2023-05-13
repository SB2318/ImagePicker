package com.sb2318.imagepicker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sb2318.imagepicker.databinding.FragmentUploadPageBinding
import com.sb2318.imagepicker.viewModel.ImagePickerViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: FragmentUploadPageBinding
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1
    private var selectedImageUri: Uri?= null
    private val PICK_IMAGE_REQUEST = 22

    val viewModel by lazy {
        ViewModelProvider(this)[ImagePickerViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.fragment_upload_page)

        binding.fab.setOnClickListener {
            startActivity(Intent(this@MainActivity,GalleryPageActivity::class.java))
        }

        binding.openGalleryBtn.setOnClickListener {
            requestStoragePermission()
        }

         binding.uploadBtn.setOnClickListener {


             selectedImageUri?.let {
                 viewModel.uploadImage(it,this@MainActivity)
             }
         }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("SELECT IMAGE","ON ACTIVITY RESULT ")
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            Log.d("SELECT IMAGE","DATA GET")
            selectedImageUri = data.data!!
            Glide.with(this).load(selectedImageUri)
                .into(binding.previewImage)

            binding.uploadBtn.setBackgroundResource(R.drawable.blue_button_bg)

        }
        else{
            Log.d("SELECT IMAGE","DATA NULL")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                openGallery()
            } else {
                // Permission is not granted
                Toast.makeText(
                    this@MainActivity,
                    "Permission Denied!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted
            openGallery()
        } else {
            // Permission is not granted yet
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_CODE
            )
            openGallery()
        }
    }

    private fun openGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }




}
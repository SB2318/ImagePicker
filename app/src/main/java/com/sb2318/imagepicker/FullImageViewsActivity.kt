package com.sb2318.imagepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sb2318.imagepicker.databinding.FragmentUploadPageBinding

class FullImageViewsActivity : AppCompatActivity() {

    private lateinit var binding: FragmentUploadPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.fragment_upload_page)

        binding.uploadBtn.visibility= View.GONE
        binding.openGalleryBtn.visibility = View.GONE
        binding.fab.visibility= View.GONE

        binding.root.setBackgroundColor(ContextCompat.getColor(this,R.color.black))

        intent?.let{

            val imgUrl= it.getStringExtra("IMAGE_URL")

            Glide.with(this).load(imgUrl)
                .into(binding.previewImage)
        }

    }
}
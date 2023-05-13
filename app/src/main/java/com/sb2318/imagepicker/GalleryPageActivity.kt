package com.sb2318.imagepicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sb2318.imagepicker.databinding.FragmentGalleryPageBinding
import com.sb2318.imagepicker.view.adapter.GallaryAdapter
import com.sb2318.imagepicker.viewModel.ImagePickerViewModel

class GalleryPageActivity : AppCompatActivity(), GallaryAdapter.ImageClickListener {

    private lateinit var binding: FragmentGalleryPageBinding

    val viewModel by lazy {
        ViewModelProvider(this)[ImagePickerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.fragment_gallery_page)

        viewModel.getImages()
        viewModel.imageList.observe(this) {

            it?.let{
                item->

                if(item.isEmpty()){
                    binding.noItemMessage.visibility = View.VISIBLE
                    binding.recyclerView.visibility= View.GONE
                }else {
                    val adapter = GallaryAdapter(item, this, this)
                    val lm = GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false)

                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = lm
                    binding.recyclerView.setHasFixedSize(true)
                }

            }
        }

    }

    override fun onImageClick(imgUrl: String) {

        val intent = Intent(this@GalleryPageActivity,FullImageViewsActivity::class.java)
        intent.putExtra("IMAGE_URL",imgUrl)
        startActivity(intent)
    }
}
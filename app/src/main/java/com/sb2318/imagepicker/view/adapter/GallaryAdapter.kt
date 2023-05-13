package com.sb2318.imagepicker.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sb2318.imagepicker.R
import com.sb2318.imagepicker.databinding.ImageItemLayoutBinding
import com.sb2318.imagepicker.model.Image

class GallaryAdapter(private val images:List<Image>,private val context:Context
                      ,private val listener:ImageClickListener): RecyclerView.Adapter<GallaryAdapter.ImageHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {


        val binding= DataBindingUtil.inflate<ImageItemLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.image_item_layout,parent,false)

        return ImageHolder(binding)
    }



    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
       holder.bind(images[position])
    }

    inner class ImageHolder(private val binding:ImageItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root){


        fun bind(image: Image) {

            Glide.with(context).load(image.imageUrl)
                .into(binding.imageView)

            binding.imageView.setOnClickListener {
                listener.onImageClick(image.imageUrl)
            }
        }

    }

    interface ImageClickListener{

        fun onImageClick(imgUrl:String)
    }
}
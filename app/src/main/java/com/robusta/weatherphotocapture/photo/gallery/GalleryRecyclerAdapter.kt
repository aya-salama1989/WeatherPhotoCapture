package com.robusta.weatherphotocapture.photo.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robusta.weatherphotocapture.R
import kotlinx.android.synthetic.main.item_image.view.*

class GalleryRecyclerAdapter (private val photoClickListener:PhotoClickListener):
    ListAdapter<Uri, GalleryRecyclerAdapter.ImageViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Uri,
            newItem: Uri
        ): Boolean {
            return oldItem.equals(newItem)
        }
    }


    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(image: Uri) {
            Glide.with(itemView.context)
                .load(image)
                .into(itemView.ivImage)
            itemView.setOnClickListener {
                photoClickListener.clickListener(image)
            }
        }
    }
}

class PhotoClickListener(val clickListener: (imageURI: Uri) -> Unit){
    fun onClick(imageURI: Uri) = clickListener(imageURI)
}
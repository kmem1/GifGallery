package com.example.gifgallery.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gifgallery.R
import com.example.gifgallery.databinding.GalleryItemBinding
import com.example.gifgallery.domain.models.GifModel

class GifsAdapter : PagingDataAdapter<GifModel, GifsAdapter.ViewHolder>(GifModelComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: GalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GifModel) {
            Glide.with(binding.root.context).asGif().centerCrop()
                .placeholder(R.drawable.gif_placeholder)
                .load(model.url).into(binding.ivGif)
        }
    }

    object GifModelComparator : DiffUtil.ItemCallback<GifModel>() {
        override fun areItemsTheSame(oldItem: GifModel, newItem: GifModel): Boolean =
            oldItem.url == newItem.url


        override fun areContentsTheSame(oldItem: GifModel, newItem: GifModel): Boolean =
            oldItem == newItem
    }
}
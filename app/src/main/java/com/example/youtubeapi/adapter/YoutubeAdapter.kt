package com.example.youtubeapi.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.youtubeapi.databinding.ItemLayoutBinding
import com.example.youtubeapi.model.Item

class YoutubeAdapter : ListAdapter<Item, YoutubeAdapter.YoutubeViewHolder>(DiffCallBack()) {
    lateinit var onClick: (Item) -> Unit

    private class DiffCallBack : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        return YoutubeViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class YoutubeViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            with(binding) {
                Glide.with(imageView)
                    .load(item.snippet.thumbnails.default.url)
                    .addListener(listener {
                        binding.prb.isVisible = false
                    })
                    .into(imageView)
                textView.text = item.snippet.title
            }
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    private fun listener(onSuccess: () -> Unit) = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d("@@@", e?.message.toString())
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess()
            return false
        }
    }
}
package com.dicoding.picodiploma.loginwithanimation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.StoryItemBinding

@Suppress("DEPRECATION")
class StoryAdapter(private val onItemClick: (ListStoryItem) -> Unit) : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    inner class MyViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var imgPhoto: ImageView = binding.ivDetailPhoto
        private var tvName: TextView = binding.tvDetailName
        private var tvDescription: TextView = binding.tvDetailDescription
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onItemClick(item)
                    }
                }
            }
        }

        fun bind(item: ListStoryItem){
            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .into(binding.ivDetailPhoto)
            binding.tvDetailName.text = item.name
            binding.tvDetailDescription.text = item.description

            imgPhoto.transitionName = "photo"
            tvName.transitionName = "name"
            tvDescription.transitionName = "description"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
